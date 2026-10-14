package com.flora.controller;

import com.flora.entity.Payment;
import com.flora.services.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Получить все платежи
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Получить платёж по ID заказа
    @GetMapping("/{orderId}")
    public Optional<Payment> getPaymentByOrderId(
            @PathVariable int orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    // Добавить платёж
    @PostMapping
    public int addPayment(@RequestBody Payment payment) {
        return paymentService.addPayment(payment);
    }

    // Изменить платёж по ID заказа
    @PutMapping("/{orderId}")
    public int updatePayment(
            @PathVariable int orderId,
            @RequestBody Payment payment) {
        return paymentService.updatePaymentByOrderId(orderId, payment);
    }

    // Удалить платёж по ID заказа
    @DeleteMapping("/{orderId}")
    public int deletePayment(@PathVariable int orderId) {
        return paymentService.deletePaymentByOrderId(orderId);
    }
}