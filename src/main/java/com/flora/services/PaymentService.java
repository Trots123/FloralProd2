package com.flora.services;

import com.flora.entity.Order;
import com.flora.entity.Payment;
import com.flora.entity.ProductFlor;
import com.flora.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Получить все платежи
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Получить платёж по ID заказа
    public Payment getPaymentByOrderId(int orderId) {
        return paymentRepository.getReferenceById(orderId);
    }

    // Добавить платёж
    public void addPayment(Payment payment) {
        paymentRepository.saveAndFlush(payment);
    }

    // Изменить платёж
    @Transactional
    public boolean updatePaymentByOrderId(int orderId, Payment payment) {

        return paymentRepository.findById(orderId)
                .map(existing -> {
                    existing.setFullCost(payment.getFullCost());
                    existing.setPaid(payment.isPaid());
                    return true;
                })
                .orElse(false);
    }


    // Удалить платёж
    public boolean deletePaymentByOrderId(int orderId) {
        return paymentRepository.deletePayment(orderId) > 0;
    }

}