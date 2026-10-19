package com.flora.repository;

import com.flora.FlorDTO.OrderHistoryDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class CheckoutRepository {

    private final JdbcTemplate jdbcTemplate;

    public CheckoutRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. Найти клиента и заблокировать его строку
    public Long findClientIdForUpdate(String phoneNumber) {
        String sql = """
                SELECT id
                FROM client_florr
                WHERE phone_number = ?
                FOR UPDATE
                """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? rs.getLong("id") : null,
                phoneNumber
        );
    }

    // Найти клиента без блокировки (для истории)
    public Long findClientId(String phoneNumber) {
        String sql = """
                SELECT id
                FROM client_florr
                WHERE phone_number = ?
                """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? rs.getLong("id") : null,
                phoneNumber
        );
    }

    // 2. Найти открытую корзину и заблокировать её
    public Long findOpenCartForUpdate(long clientId) {
        String sql = """
                SELECT id
                FROM cart
                WHERE user_id = ?
                  AND dral_ststus = false
                ORDER BY id
                LIMIT 1
                FOR UPDATE
                """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? rs.getLong("id") : null,
                clientId
        );
    }

    // 3. Получить товары корзины
    public List<CartProduct> getCartProducts(long cartId) {
        String sql = """
                SELECT
                    prod_id,
                    SUM(quantity) AS quantity
                FROM item_cart
                WHERE cart_id = ?
                GROUP BY prod_id
                ORDER BY prod_id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new CartProduct(
                        rs.getLong("prod_id"),
                        rs.getLong("quantity")
                ),
                cartId
        );
    }

    // 4. Получить товар и заблокировать его
    public ProductData findProductForUpdate(long prodId) {
        String sql = """
                SELECT
                    name,
                    price,
                    stock_quantity
                FROM product_flor
                WHERE id = ?
                FOR UPDATE
                """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next()
                        ? new ProductData(
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity")
                )
                        : null,
                prodId
        );
    }

    // 5. Списать деньги с клиента
    public int withdrawClientBalance(
            long clientId,
            BigDecimal amount
    ) {
        String sql = """
                UPDATE client_florr
                SET balance = balance - ?
                WHERE id = ?
                  AND balance >= ?
                """;

        return jdbcTemplate.update(
                sql,
                amount,
                clientId,
                amount
        );
    }

    // 6. Уменьшить остаток товара
    public int decreaseProductStock(
            long prodId,
            long quantity
    ) {
        String sql = """
                UPDATE product_flor
                SET stock_quantity = stock_quantity - ?
                WHERE id = ?
                  AND stock_quantity >= ?
                """;

        return jdbcTemplate.update(
                sql,
                quantity,
                prodId,
                quantity
        );
    }

    // 7. Создать заказ и вернуть его ID
    public Long createOrder(
            long cartId,
            BigDecimal totalPrice,
            String deliveryAddress
    ) {
        String sql = """
                INSERT INTO "order" (
                    cart_id,
                    total_price,
                    delivery_address
                )
                VALUES (?, ?, ?)
                RETURNING id
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                cartId,
                totalPrice,
                deliveryAddress
        );
    }

    // 8. Сохранить оплату
    public void savePayment(
            long orderId,
            BigDecimal totalPrice
    ) {
        String sql = """
                INSERT INTO payment (
                    order_id,
                    full_cost,
                    paid
                )
                VALUES (?, ?, true)
                """;

        jdbcTemplate.update(
                sql,
                orderId,
                totalPrice
        );
    }

    // 9. Закрыть корзину
    public int closeCart(
            long cartId,
            long clientId
    ) {
        String sql = """
                UPDATE cart
                SET dral_ststus = true
                WHERE id = ?
                  AND user_id = ?
                  AND dral_ststus = false
                """;

        return jdbcTemplate.update(
                sql,
                cartId,
                clientId
        );
    }

    // 10. Получить историю заказов клиента
    public List<OrderData> getClientOrders(long clientId) {
        String sql = """
                SELECT
                    o.id,
                    o.cart_id,
                    o.total_price,
                    o.created_at,
                    o.delivery_address,
                    p.paid
                FROM "order" o
                JOIN cart c
                    ON c.id = o.cart_id
                JOIN payment p
                    ON p.order_id = o.id
                WHERE c.user_id = ?
                ORDER BY o.id DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new OrderData(
                        rs.getLong("id"),
                        rs.getLong("cart_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getBoolean("paid"),
                        rs.getObject(
                                "created_at",
                                OffsetDateTime.class
                        ),
                        rs.getString("delivery_address")
                ),
                clientId
        );
    }

    // Вспомогательные структуры для передачи данных
    public record CartProduct(
            long prodId,
            long quantity
    ) {}

    public record ProductData(
            String name,
            BigDecimal price,
            int stockQuantity
    ) {}

    public record OrderData(
            long orderId,
            long cartId,
            BigDecimal totalPrice,
            boolean paid,
            OffsetDateTime createdAt,
            String deliveryAddress
    ) {}
}