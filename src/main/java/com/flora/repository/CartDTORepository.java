package com.flora.repository;

import com.flora.FlorDTO.CartDTO;
import com.flora.entity.Cart;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class CartDTORepository {

    private final JdbcTemplate jdbcTemplate;

    public CartDTORepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<CartDTO> getForCartFrontByClientId(int clientId) {
        String sql = """
        SELECT
            cf.id AS client_id,
            pf.id AS prod_id,
            pf.name AS prod_name,
            ic.quantity AS quantity,
            pf.price AS price,
            pf.price * ic.quantity AS total
        FROM client_florr cf
        LEFT JOIN cart c ON c.user_id = cf.id
        LEFT JOIN item_cart ic ON ic.cart_id = c.id
        LEFT JOIN product_flor pf ON ic.prod_id = pf.id
        WHERE cf.id = ?
        """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new CartDTO(
                        rs.getInt("client_id"),
                        rs.getInt("prod_id"),
                        rs.getString("prod_name"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("price"),
                        rs.getBigDecimal("total")
                ),
                clientId
        );
    }




}






