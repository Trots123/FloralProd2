package com.flora.repository;

import com.flora.entity.Cart;
import com.flora.entity.ItemCart;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemCartRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CartRepository cartRepository;

    public ItemCartRepository(
            JdbcTemplate jdbcTemplate,
            CartRepository cartRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.cartRepository = cartRepository;
    }

    // CREATE: создать позицию корзины
    public int createItemCart(ItemCart itemCart) {
        String sql = """
                INSERT INTO item_cart (cart_id, prod_id, quantity)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                itemCart.getCartId(),
                itemCart.getProdId(),
                itemCart.getQuantity()
        );
    }

    // READ: получить все позиции корзины
    public List<ItemCart> getAllItemCarts() {
        String sql = """
                SELECT id, cart_id, prod_id, quantity
                FROM item_cart
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            int cartId = rs.getInt("cart_id");

            Cart cart = cartRepository.getCartById(cartId)
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Cart not found: " + cartId));

            return new ItemCart(
                    rs.getInt("id"),
                    cart,
                    rs.getInt("prod_id"),
                    rs.getInt("quantity")
            );
        });
    }

    // READ: получить позицию по ID
    public Optional<ItemCart> getItemCartById(int id) {
        String sql = """
                SELECT id, cart_id, prod_id, quantity
                FROM item_cart
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }

            int cartId = rs.getInt("cart_id");

            Cart cart = cartRepository.getCartById(cartId)
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Cart not found: " + cartId));

            return Optional.of(new ItemCart(
                    rs.getInt("id"),
                    cart,
                    rs.getInt("prod_id"),
                    rs.getInt("quantity")
            ));
        }, id);
    }

    // UPDATE: обновить позицию корзины
    public int updateItemCart(ItemCart itemCart) {
        String sql = """
                UPDATE item_cart
                SET cart_id = ?, prod_id = ?, quantity = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                itemCart.getCartId(),
                itemCart.getProdId(),
                itemCart.getQuantity(),
                itemCart.getId()
        );
    }

    // DELETE: удалить позицию корзины
    public int deleteItemCart(int id) {
        String sql = """
                DELETE FROM item_cart
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }
    // Получить все позиции конкретной корзины
    public List<ItemCart> getItemsByCartId(int cartId) {
        String sql = """
            SELECT id, cart_id, prod_id, quantity
            FROM item_cart
            WHERE cart_id = ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Cart cart = cartRepository.getCartById(cartId)
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Cart not found: " + cartId));

            return new ItemCart(
                    rs.getInt("id"),
                    cart,
                    rs.getInt("prod_id"),
                    rs.getInt("quantity")
            );
        }, cartId);
    }
    // Получить общее количество товара в корзине
    public int getQuantityInCart(int cartId, int prodId, int excludeItemId) {
        String sql = """
            SELECT COALESCE(SUM(quantity), 0)
            FROM item_cart
            WHERE cart_id = ?
              AND prod_id = ?
              AND id <> ?
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                cartId,
                prodId,
                excludeItemId
        );
    }

}