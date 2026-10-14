package com.flora.services;

import com.flora.entity.Payment;
import com.flora.repository.PaymentRepository;
import org.springframework.stereotype.Service;

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
        return paymentRepository.getAllPayments();
    }

    // Получить платёж по ID заказа
    public Optional<Payment> getPaymentByOrderId(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "ID заказа должен быть больше нуля"
            );
        }

        return paymentRepository.getPaymentByOrderId(orderId);
    }

    // Добавить платёж
    public int addPayment(Payment payment) {
        validatePayment(payment);

        return paymentRepository.addPayment(payment);
    }

    // Изменить платёж
    public int updatePaymentByOrderId(int orderId, Payment payment) {
        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "ID заказа должен быть больше нуля"
            );
        }

        validatePayment(payment);

        return paymentRepository.updatePaymentByOrderId(orderId, payment);
    }

    // Удалить платёж
    public int deletePaymentByOrderId(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "ID заказа должен быть больше нуля"
            );
        }

        return paymentRepository.deletePaymentByOrderId(orderId);
    }

    // Проверка данных платежа
    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException(
                    "Платёж не должен быть null"
            );
        }

        if (payment.getOrderId() <= 0) {
            throw new IllegalArgumentException(
                    "ID заказа должен быть больше нуля"
            );
        }

        BigDecimal fullCost = payment.getFullCost();

        if (fullCost == null || fullCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Сумма платежа должна быть больше нуля"
            );
        }
    }
}