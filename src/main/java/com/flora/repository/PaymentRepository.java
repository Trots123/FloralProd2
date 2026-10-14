package com.flora.repository;


import com.flora.entity.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Получить все платежи
    public List<Payment> getAllPayments() {
        String sql = """
                SELECT full_cost, paid, order_id
                FROM payment
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Payment.builder()
                        .fullCost(rs.getBigDecimal("full_cost"))
                        .paid(rs.getBoolean("paid"))
                        .orderId(rs.getInt("order_id"))
                        .build()
        );
    }

    // Получить платёж по ID заказа
    public Optional<Payment> getPaymentByOrderId(int orderId) {
        String sql = """
                SELECT full_cost, paid, order_id
                FROM payment
                WHERE order_id = ?
                """;

        List<Payment> payments = jdbcTemplate.query(sql, (rs, rowNum) ->
                        Payment.builder()
                                .fullCost(rs.getBigDecimal("full_cost"))
                                .paid(rs.getBoolean("paid"))
                                .orderId(rs.getInt("order_id"))
                                .build(),
                orderId
        );

        return payments.stream().findFirst();
    }

    // Добавить платёж
    public int addPayment(Payment payment) {
        String sql = """
                INSERT INTO payment (full_cost, paid, order_id)
                VALUES (?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                payment.getFullCost(),
                payment.isPaid(),
                payment.getOrderId()
        );
    }

    // Изменить платёж
    public int updatePaymentByOrderId(int orderId, Payment payment) {
        String sql = """
                UPDATE payment
                SET full_cost = ?, paid = ?
                WHERE order_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                payment.getFullCost(),
                payment.isPaid(),
                orderId
        );
    }

    // Удалить платёж
    public int deletePaymentByOrderId(int orderId) {
        String sql = """
                DELETE FROM payment
                WHERE order_id = ?
                """;

        return jdbcTemplate.update(sql, orderId);
    }
}