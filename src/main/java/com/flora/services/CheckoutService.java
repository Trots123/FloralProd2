package com.flora.services;

import com.flora.FlorDTO.CheckoutRequestDTO;
import com.flora.FlorDTO.CheckoutResponseDTO;
import com.flora.FlorDTO.OrderHistoryDTO;
import com.flora.entity.OrderItem;
import com.flora.repository.CheckoutRepository;
import com.flora.repository.OrderItemRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final OrderItemRepository orderItemRepository;

    public CheckoutService(
            CheckoutRepository checkoutRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.checkoutRepository = checkoutRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // =========================================
    // ОФОРМЛЕНИЕ ЗАКАЗА
    // =========================================

    @Transactional
    public CheckoutResponseDTO checkout(
            String phoneNumber,
            CheckoutRequestDTO request
    ) {

        // 1. Проверяем адрес доставки
        if (request == null
                || request.deliveryAddress() == null
                || request.deliveryAddress().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Укажите адрес доставки"
            );
        }

        String deliveryAddress =
                request.deliveryAddress().trim();

        // 2. Находим клиента
        Long clientId =
                checkoutRepository.findClientIdForUpdate(
                        phoneNumber
                );

        if (clientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Клиент не найден"
            );
        }

        // 3. Находим открытую корзину
        Long cartId =
                checkoutRepository.findOpenCartForUpdate(
                        clientId
                );

        if (cartId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Открытая корзина не найдена"
            );
        }

        // 4. Получаем товары корзины
        List<CheckoutRepository.CartProduct> products =
                checkoutRepository.getCartProducts(cartId);

        if (products.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Корзина пустая"
            );
        }

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        // 5. Проверяем каждый товар
        for (CheckoutRepository.CartProduct product : products) {

            if (product.quantity() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Некорректное количество товара"
                );
            }

            CheckoutRepository.ProductData data =
                    checkoutRepository.findProductForUpdate(
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

            // Создаём позицию будущего заказа
            OrderItem item = new OrderItem(
                    0,
                    0,
                    product.prodId(),
                    data.name(),
                    product.quantity(),
                    data.price()
            );

            orderItems.add(item);

            // Считаем стоимость позиции
            BigDecimal itemTotal = data.price().multiply(
                    BigDecimal.valueOf(product.quantity())
            );

            totalPrice = totalPrice.add(itemTotal);
        }

        // 6. Списываем деньги клиента
        int updatedClient =
                checkoutRepository.withdrawClientBalance(
                        clientId,
                        totalPrice
                );

        if (updatedClient != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недостаточно денег на балансе"
            );
        }

        // 7. Уменьшаем остатки товаров
        for (OrderItem item : orderItems) {

            int updatedProduct =
                    checkoutRepository.decreaseProductStock(
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

        // 8. Создаём заказ
        Long orderId = checkoutRepository.createOrder(
                cartId,
                totalPrice,
                deliveryAddress
        );

        if (orderId == null) {
            throw new IllegalStateException(
                    "Не удалось создать заказ"
            );
        }

        // 9. Сохраняем товары заказа
        for (OrderItem item : orderItems) {

            item.setOrderId(orderId);

            orderItemRepository.saveOrderItem(item);
        }

        // 10. Сохраняем оплату
        checkoutRepository.savePayment(
                orderId,
                totalPrice
        );

        // 11. Закрываем корзину
        int closed = checkoutRepository.closeCart(
                cartId,
                clientId
        );

        if (closed != 1) {
            throw new IllegalStateException(
                    "Не удалось закрыть корзину"
            );
        }

        // 12. Возвращаем результат
        return new CheckoutResponseDTO(
                orderId,
                cartId,
                totalPrice,
                "Покупка успешно оформлена"
        );
    }

    // =========================================
    // ИСТОРИЯ ЗАКАЗОВ КЛИЕНТА
    // =========================================

    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getMyOrders(
            String phoneNumber
    ) {

        // 1. Находим клиента
        Long clientId =
                checkoutRepository.findClientId(
                        phoneNumber
                );

        if (clientId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Клиент не найден"
            );
        }

        // 2. Получаем заказы клиента
        List<CheckoutRepository.OrderData> orders =
                checkoutRepository.getClientOrders(
                        clientId
                );

        List<OrderHistoryDTO> history = new ArrayList<>();

        // 3. Формируем историю с товарами
        for (CheckoutRepository.OrderData order : orders) {

            List<OrderHistoryDTO.OrderItemDTO> items =
                    orderItemRepository
                            .getItemsByOrderId(order.orderId())
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

            OrderHistoryDTO dto = new OrderHistoryDTO(
                    order.orderId(),
                    order.cartId(),
                    order.totalPrice(),
                    order.paid(),
                    order.createdAt(),
                    order.deliveryAddress(),
                    items
            );

            history.add(dto);
        }

        return history;
    }
}