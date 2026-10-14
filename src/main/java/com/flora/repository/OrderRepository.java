package com.flora.repository;

import com.flora.entity.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE — создать заказ
    public int createOrder(Order order) {
        String sql = """
                INSERT INTO "order" (cart_id, total_price)
                VALUES (?, ?)
                RETURNING id
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                order.getCartId(),
                order.getTotalPrice()
        );
    }

    // READ — получить все заказы
    public List<Order> getAllOrders() {
        String sql = """
                SELECT id, cart_id, total_price
                FROM "order"
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Order(
                        rs.getInt("id"),
                        rs.getInt("cart_id"),
                        rs.getBigDecimal("total_price")
                )
        );
    }

    // READ — получить заказ по ID
    public Optional<Order> getOrderById(int id) {
        String sql = """
                SELECT id, cart_id, total_price
                FROM "order"
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Order(
                    rs.getInt("id"),
                    rs.getInt("cart_id"),
                    rs.getBigDecimal("total_price")
            ));
        }, id);
    }

    // UPDATE — обновить заказ
    public int updateOrder(Order order) {
        String sql = """
                UPDATE "order"
                SET cart_id = ?, total_price = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                order.getCartId(),
                order.getTotalPrice(),
                order.getId()
        );
    }

    // DELETE — удалить заказ
    public int deleteOrder(int id) {
        String sql = """
                DELETE FROM "order"
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }
}