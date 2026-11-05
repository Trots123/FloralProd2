package com.flora.services;

import com.flora.entity.Cart;
import com.flora.entity.Order;
import com.flora.entity.ProductFlor;
import com.flora.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
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
        return orderRepository.findAll();
    }

    // Получить заказ по ID
    public Order getOrderById(int id) {
        return orderRepository.getReferenceById(id);
    }

    // Создать заказ
    public void createOrder(Order order) {
        orderRepository.saveAndFlush(order);
    }


    // Обновление заказа
    public boolean updateOrder(int id, Order order) {
        return orderRepository.updateOrder(
                id,
                order.getCart(),
                order.getTotalPrice(),
                order.getDeliveryAddress()
        ) > 0;
    }

    // Удалить заказ
    public boolean deleteOrder(int id) {
        return orderRepository.deleteOrder(id) > 0;
    }

}