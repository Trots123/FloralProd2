package com.flora.repository.DTO;

import com.flora.FlorDTO.CartDTO;
import com.flora.entity.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDTORepository extends JpaRepository<ItemCart, Integer> {

    @Query("""
        SELECT new com.flora.FlorDTO.CartDTO(
            ic.id,
            c.client.id,
            p.id,
            p.name,
            ic.quantity,
            p.price,
            p.price * ic.quantity
        )
        FROM ItemCart ic
        JOIN ic.cart c
        JOIN ic.productFlor p
        WHERE c.client.id = :clientId
          AND c.dralStstus = false
        ORDER BY ic.id
        """)
    List<CartDTO> getForCartFrontByClientId(
            @Param("clientId") int clientId
    );
}