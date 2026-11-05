package com.flora.repository;


import com.flora.entity.Cart;
import com.flora.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order , Integer> {

    @Query("""
    SELECT o FROM Order o
    WHERE o.cart.client.id = :clientId
    ORDER BY o.id DESC
    """)
    List<Order> findByCartClientIdOrderByIdDesc(
            @Param("clientId") int clientId
    );
    @Modifying
    @Query("""
    UPDATE Order o
    SET o.cart = :cart,
        o.totalPrice = :totalPrice,
        o.deliveryAddress = :deliveryAddress
    WHERE o.id = :id
    """)
    int updateOrder(
            @Param("id") int id,
            @Param("cart") Cart cart,
            @Param("totalPrice") BigDecimal totalPrice,
            @Param("deliveryAddress") String deliveryAddress
    );

    @Modifying
    @Query("""
            DELETE FROM Order o
            WHERE o.id = :id""")
    int deleteOrder(@Param("id") int id);


}