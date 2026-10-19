package com.flora.repository;

import com.flora.FlorDTO.CartDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartDTORepository {

    private final JdbcTemplate jdbcTemplate;

    public CartDTORepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Получить товары из открытой корзины клиента
    public List<CartDTO> getForCartFrontByClientId(int clientId) {

        String sql = """
                SELECT
                    cf.id AS client_id,
                    pf.id AS prod_id,
                    ic.id AS item_id,
                    pf.name AS prod_name,
                    ic.quantity AS quantity,
                    pf.price AS price,
                    pf.price * ic.quantity AS total
                FROM client_florr cf
                JOIN cart c ON c.user_id = cf.id
                JOIN item_cart ic ON ic.cart_id = c.id
                JOIN product_flor pf ON ic.prod_id = pf.id
                WHERE cf.id = ?
                  AND c.dral_ststus = false
                ORDER BY ic.id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {

                    CartDTO dto = new CartDTO(
                            rs.getInt("client_id"),
                            rs.getInt("prod_id"),
                            rs.getString("prod_name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("price"),
                            rs.getBigDecimal("total")
                    );

                    dto.setItemId(rs.getInt("item_id"));

                    return dto;
                },
                clientId
        );
    }
}