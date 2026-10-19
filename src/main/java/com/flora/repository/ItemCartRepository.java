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

    // Найти товар в конкретной корзине
    public Optional<ItemCart> getItemByCartIdAndProdId(
            int cartId,
            int prodId) {

        String sql = """
                SELECT id, cart_id, prod_id, quantity
                FROM item_cart
                WHERE cart_id = ?
                  AND prod_id = ?
                LIMIT 1
                """;

        return jdbcTemplate.query(
                sql,
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    ItemCart itemCart = new ItemCart(
                            rs.getInt("id"),
                            rs.getInt("cart_id"),
                            rs.getInt("prod_id"),
                            rs.getInt("quantity")
                    );

                    return Optional.of(itemCart);
                },
                cartId,
                prodId
        );
    }

    public int deleteMyItem(int itemId, int clientId) {

        String sql = """
                DELETE FROM item_cart ic
                USING cart c
                WHERE ic.cart_id = c.id
                  AND ic.id = ?
                  AND c.user_id = ?
                  AND c.dral_ststus = false
                """;

        return jdbcTemplate.update(sql, itemId, clientId);
    }

    public int updateMyItemQuantity(
            int itemId,
            int clientId,
            int quantity
    ) {

        String sql = """
                UPDATE item_cart ic
                SET quantity = ?
                FROM cart c
                WHERE ic.cart_id = c.id
                  AND ic.id = ?
                  AND c.user_id = ?
                  AND c.dral_ststus = false
                """;

        return jdbcTemplate.update(
                sql,
                quantity,
                itemId,
                clientId
        );
    }

    public Optional<ItemCart> getMyItemById(
            int itemId,
            int clientId
    ) {

        String sql = """
                SELECT ic.id, ic.cart_id, ic.prod_id, ic.quantity
                FROM item_cart ic
                JOIN cart c ON ic.cart_id = c.id
                WHERE ic.id = ?
                  AND c.user_id = ?
                  AND c.dral_ststus = false
                """;

        return jdbcTemplate.query(
                sql,
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    return Optional.of(new ItemCart(
                            rs.getInt("id"),
                            rs.getInt("cart_id"),
                            rs.getInt("prod_id"),
                            rs.getInt("quantity")
                    ));
                },
                itemId,
                clientId
        );
    }
}














/*

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
                    rs.getInt("cart_id"),
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
                    rs.getInt("cart_id"),
                    rs.getInt("prod_id"),
                    rs.getInt("quantity")
            ));
        }, id);
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
                    rs.getInt("cart_id"),
                    rs.getInt("prod_id"),
                    rs.getInt("quantity")
            );
        }, cartId);
    }
 */