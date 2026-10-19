package com.flora.repository;

import com.flora.entity.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Сохранить купленный товар
    public int saveOrderItem(OrderItem item) {

        String sql = """
                INSERT INTO order_item
                    (order_id, prod_id, prod_name, quantity, price)
                VALUES (?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                item.getOrderId(),
                item.getProdId(),
                item.getProdName(),
                item.getQuantity(),
                item.getPrice()
        );
    }

    // Получить все товары конкретного заказа
    public List<OrderItem> getItemsByOrderId(long orderId) {

        String sql = """
                SELECT
                    id,
                    order_id,
                    prod_id,
                    prod_name,
                    quantity,
                    price
                FROM order_item
                WHERE order_id = ?
                ORDER BY id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new OrderItem(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getLong("prod_id"),
                        rs.getString("prod_name"),
                        rs.getLong("quantity"),
                        rs.getBigDecimal("price")
                ),
                orderId
        );
    }
}