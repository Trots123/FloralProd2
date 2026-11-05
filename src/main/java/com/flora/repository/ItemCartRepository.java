package com.flora.repository;

import com.flora.entity.ItemCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemCartRepository
        extends JpaRepository<ItemCart, Integer> {

    // Найти товар в конкретной корзине
    Optional<ItemCart> findFirstByCart_IdAndProductFlor_Id(int cartId, int prodId);

    @Query("""
    SELECT i FROM ItemCart i
    WHERE i.cart.id = :cartId
    """)
    List<ItemCart> findByCartId(
            @Param("cartId") int cartId
    );
    // Получить количество товара в корзине,
    // исключая конкретную позицию
    @Query("""
        SELECT COALESCE(SUM(ic.quantity), 0)
        FROM ItemCart ic
        WHERE ic.cart.id = :cartId
          AND ic.productFlor.id = :prodId
          AND ic.id <> :excludeItemId
        """)
    Long getQuantityInCart(
            @Param("cartId") int cartId,
            @Param("prodId") int prodId,
            @Param("excludeItemId") int excludeItemId
    );

    // Найти позицию только в активной корзине клиента
    @Query("""
        SELECT ic
        FROM ItemCart ic
        WHERE ic.id = :itemId
          AND ic.cart.client.id = :clientId
          AND ic.cart.dralStstus = false
        """)
    Optional<ItemCart> getMyItemById(@Param("itemId") int itemId, @Param("clientId") int clientId);

    // Удалить позицию только из своей активной корзины
    @Modifying
    @Query("""
        DELETE FROM ItemCart ic
        WHERE ic.id = :itemId
          AND ic.cart.id IN (
              SELECT c.id
              FROM Cart c
              WHERE c.client.id = :clientId
                AND c.dralStstus = false
          )
        """)
    int deleteMyItem(@Param("itemId") int itemId, @Param("clientId") int clientId);

    // Изменить количество только в своей активной корзине
    @Modifying
    @Query("""
        UPDATE ItemCart ic
        SET ic.quantity = :quantity
        WHERE ic.id = :itemId
          AND ic.cart.id IN (
              SELECT c.id
              FROM Cart c
              WHERE c.client.id = :clientId
                AND c.dralStstus = false
          )
        """)
    int updateMyItemQuantity(@Param("itemId") int itemId, @Param("clientId") int clientId, @Param("quantity") int quantity
    );
}