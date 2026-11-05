package com.flora.repository;

import com.flora.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @EntityGraph(attributePaths = {"client"})
    @Query("""
        SELECT c
        FROM Cart c
        WHERE c.client.id = :clientId
          AND c.dralStstus = false
        """)

    Optional<Cart> getCartByClientId(
            @Param("clientId") int clientId
    );
}









    /*
    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<Cart> getCartByClientId(int clientId) {
        String sql = """
            
                SELECT id, user_id, dral_ststus
            FROM cart
            WHERE user_id = ?
              AND dral_ststus = false
            LIMIT 1
            """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next()
                        ? Optional.of(new Cart(
                        rs.getInt("id"),
                        (ClientFlorr) rs,
                        rs.getBoolean("dral_ststus")
                ))
                        : Optional.empty(),
                clientId
        );
    }

    // создание корзины
    public int createCart(int clientId) {
        String sql =
                """
            INSERT INTO cart (user_id,
                dral_
                       VALUES (?, false)
                            RETURNING id
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                clientId
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
                        (ClientFlorr) rs,
                        rs.getBoolean("dral_ststus")
                ))
                        : Optional.empty(),
                id
        );
    }
}




     */