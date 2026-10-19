package com.flora.services;



import com.flora.FlorDTO.CheckoutResponseDTO;
import com.flora.FlorDTO.OrderHistoryDTO;
import com.flora.entity.OrderItem;
import com.flora.repository.OrderItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {

    private final JdbcTemplate jdbcTemplate;
    private final OrderItemRepository orderItemRepository;

    public CheckoutService(
            JdbcTemplate jdbcTemplate,
            OrderItemRepository orderItemRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public CheckoutResponseDTO checkout(String phoneNumber) {

        // 1. Блокируем клиента
        Long clientId = jdbcTemplate.query(
                """
                SELECT id
                FROM client_florr
                WHERE phone_number = ?
                FOR UPDATE
                """,
                rs -> rs.next() ? rs.getLong("id") : null,
                phoneNumber
        );

        if (clientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Клиент не найден"
            );
        }

        // 2. Находим открытую корзину
        Long cartId = jdbcTemplate.query(
                """
                SELECT id
                FROM cart
                WHERE user_id = ?
                  AND dral_ststus = false
                ORDER BY id
                LIMIT 1
                FOR UPDATE
                """,
                rs -> rs.next() ? rs.getLong("id") : null,
                clientId
        );

        if (cartId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Открытая корзина не найдена"
            );
        }

        // 3. Получаем товары из корзины
        // Одинаковые товары объединяем
        List<CartProduct> products = jdbcTemplate.query(
                """
                SELECT
                    prod_id,
                    SUM(quantity) AS quantity
                FROM item_cart
                WHERE cart_id = ?
                GROUP BY prod_id
                ORDER BY prod_id
                """,
                (rs, rowNum) -> new CartProduct(
                        rs.getLong("prod_id"),
                        rs.getLong("quantity")
                ),
                cartId
        );

        if (products.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Корзина пустая"
            );
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 4. Проверяем товары и запоминаем цены
        for (CartProduct product : products) {

            if (product.quantity() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Некорректное количество товара"
                );
            }

            ProductData data = jdbcTemplate.query(
                    """
                    SELECT name, price, stock_quantity
                    FROM product_flor
                    WHERE id = ?
                    FOR UPDATE
                    """,
                    rs -> rs.next()
                            ? new ProductData(
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("stock_quantity")
                    )
                            : null,
                    product.prodId()
            );

            if (data == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Товар не найден: " + product.prodId()
                );
            }

            if (data.price() == null
                    || data.price().signum() < 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Некорректная цена товара"
                );
            }

            if (data.stockQuantity() < product.quantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Недостаточно товара на складе: "
                                + product.prodId()
                );
            }

            OrderItem item = new OrderItem(
                    0,
                    0,
                    product.prodId(),
                    data.name(),
                    product.quantity(),
                    data.price()
            );

            orderItems.add(item);

            BigDecimal itemTotal = data.price()
                    .multiply(
                            BigDecimal.valueOf(product.quantity())
                    );

            totalPrice = totalPrice.add(itemTotal);
        }

        // 5. Списываем деньги
        int updatedClient = jdbcTemplate.update(
                """
                UPDATE client_florr
                SET balance = balance - ?
                WHERE id = ?
                  AND balance >= ?
                """,
                totalPrice,
                clientId,
                totalPrice
        );

        if (updatedClient != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недостаточно денег на балансе"
            );
        }

        // 6. Уменьшаем остатки товаров
        for (OrderItem item : orderItems) {

            int updatedProduct = jdbcTemplate.update(
                    """
                    UPDATE product_flor
                    SET stock_quantity = stock_quantity - ?
                    WHERE id = ?
                      AND stock_quantity >= ?
                    """,
                    item.getQuantity(),
                    item.getProdId(),
                    item.getQuantity()
            );

            if (updatedProduct != 1) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Недостаточно товара: "
                                + item.getProdId()
                );
            }
        }

        // 7. Создаём заказ
        Long orderId = jdbcTemplate.queryForObject(
                """
                INSERT INTO "order" (cart_id, total_price)
                VALUES (?, ?)
                RETURNING id
                """,
                Long.class,
                cartId,
                totalPrice
        );

        // 8. Сохраняем позиции заказа
        // Здесь используются цены на момент покупки
        for (OrderItem item : orderItems) {
            item.setOrderId(orderId);
            orderItemRepository.saveOrderItem(item);
        }

        // 9. Сохраняем оплату
        jdbcTemplate.update(
                """
                INSERT INTO payment (order_id, full_cost, paid)
                VALUES (?, ?, true)
                """,
                orderId,
                totalPrice
        );

        // 10. Закрываем корзину
        int closed = jdbcTemplate.update(
                """
                UPDATE cart
                SET dral_ststus = true
                WHERE id = ?
                  AND user_id = ?
                  AND dral_ststus = false
                """,
                cartId,
                clientId
        );

        if (closed != 1) {
            throw new IllegalStateException(
                    "Не удалось закрыть корзину"
            );
        }

        return new CheckoutResponseDTO(
                orderId,
                cartId,
                totalPrice,
                "Покупка успешно оформлена"
        );
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getMyOrders(String phoneNumber) {

        Long clientId = jdbcTemplate.query(
                """
                SELECT id
                FROM client_florr
                WHERE phone_number = ?
                """,
                rs -> rs.next() ? rs.getLong("id") : null,
                phoneNumber
        );

        if (clientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Клиент не найден"
            );
        }

        return jdbcTemplate.query(
                """
               
                        SELECT
                              o.id,
                              o.cart_id,
                              o.total_price,
                              o.created_at,
                              p.paid
                FROM "order" o
                JOIN cart c ON c.id = o.cart_id
                JOIN payment p ON p.order_id = o.id
                WHERE c.user_id = ?
                ORDER BY o.id DESC
                """,
                (rs, rowNum) -> {

                    long orderId = rs.getLong("id");

                    List<OrderHistoryDTO.OrderItemDTO> items =
                            orderItemRepository
                                    .getItemsByOrderId(orderId)
                                    .stream()
                                    .map(item ->
                                            new OrderHistoryDTO.OrderItemDTO(
                                                    item.getProdId(),
                                                    item.getProdName(),
                                                    item.getQuantity(),
                                                    item.getPrice(),
                                                    item.getPrice().multiply(
                                                            BigDecimal.valueOf(
                                                                    item.getQuantity()
                                                            )
                                                    )
                                            )
                                    )
                                    .toList();

                    return new OrderHistoryDTO(
                            orderId,
                            rs.getLong("cart_id"),
                            rs.getBigDecimal("total_price"),
                            rs.getBoolean("paid"),
                            rs.getObject("created_at", OffsetDateTime.class),
                            items
                    );
                },
                clientId
        );
    }

    private record CartProduct(
            long prodId,
            long quantity
    ) {
    }

    private record ProductData(
            String name,
            BigDecimal price,
            int stockQuantity
    ) {
    }
}