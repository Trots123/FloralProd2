package com.flora.repository;

import com.flora.entity.Cart;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.flora.entity.Cart;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.Optional;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<Cart> getCartByClientId(int clientId) {
        String sql = """
                SELECT id, user_id, dral_ststus
                FROM cart
                WHERE user_id = ?
                  AND dral_ststus = true
                LIMIT 1
                """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next()
                        ? Optional.of(new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("dral_ststus")
                ))
                        : Optional.empty(),
                clientId
        );
    }

    // создание корзины
    public int createCart(int clientId) {
        String sql = """
                INSERT INTO cart (user_id, dral_ststus)
                VALUES (?, true)
                RETURNING id
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                clientId
        );
    }

    //  Получить все корзины
    public List<Cart> getAllCarts() {
        String sql = """
                SELECT id, user_id, dral_ststus
                FROM cart
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("dral_ststus")
                )
        );
    }

    // Получить корзину по ID
    public Optional<Cart> getCartById(int id) {
        String sql = """
                SELECT id, user_id, dral_ststus
                FROM cart
                WHERE id = ?
                """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next()
                        ? Optional.of(new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("dral_ststus")
                ))
                        : Optional.empty(),
                id
        );
    }

    // Обновить корзину
    public int updateCart(Cart cart) {
        String sql = """
                UPDATE cart
                SET user_id = ?, dral_ststus = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                cart.getUserId(),
                cart.isDralStstus(),
                cart.getId()
        );
    }

    //  Удалить корзину
    public int deleteCart(int id) {
        String sql = """
                DELETE FROM cart
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, id);
    }
}