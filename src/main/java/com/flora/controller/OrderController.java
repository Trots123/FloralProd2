package com.flora.controller;

import com.flora.entity.Order;
import com.flora.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Получить все заказы
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Получить заказ по ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    // Создать заказ
    @PostMapping
    public int createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // Обновить заказ
    @PutMapping("/{id}")
    public int updateOrder(
            @PathVariable int id,
            @RequestBody Order order) {

        order.setId(id);
        return orderService.updateOrder(order);
    }

    // Удалить заказ
    @DeleteMapping("/{id}")
    public int deleteOrder(@PathVariable int id) {
        return orderService.deleteOrder(id);
    }
}