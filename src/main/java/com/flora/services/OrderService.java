package com.flora.services;
import com.flora.entity.Order;
import com.flora.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Получить все заказы
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    // Получить заказ по ID
    public Optional<Order> getOrderById(int id) {
        return orderRepository.getOrderById(id);
    }

    // Создать заказ
    public int createOrder(Order order) {
        if (order.getCartId() <= 0) {
            throw new IllegalArgumentException(
                    "ID корзины должен быть больше нуля"
            );
        }

        if (order.getTotalPrice() == null
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Стоимость заказа должна быть больше нуля"
            );
        }

        return orderRepository.createOrder(order);
    }

    // Обновить заказ
    public int updateOrder(Order order) {
        if (order.getId() <= 0) {
            throw new IllegalArgumentException(
                    "ID заказа должен быть больше нуля"
            );
        }

        if (order.getCartId() <= 0) {
            throw new IllegalArgumentException(
                    "ID корзины должен быть больше нуля"
            );
        }

        if (order.getTotalPrice() == null
                || order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Стоимость заказа должна быть больше нуля"
            );
        }

        return orderRepository.updateOrder(order);
    }

    // Удалить заказ
    public int deleteOrder(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID заказа должен быть больше нуля"
            );
        }

        return orderRepository.deleteOrder(id);
    }
}