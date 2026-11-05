package com.flora.repository;


import com.flora.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment , Integer> {

    // Найти платёж по ID заказа
    Optional<Payment> getPaymentByOrderId(int orderId);
}