package com.flora.repository.DTO;

import com.flora.FlorDTO.OrderHistoryItemRowDTO;
import com.flora.FlorDTO.OrderSummaryDTO;
import com.flora.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderHistoryDTORepository extends JpaRepository<Order, Integer> {

    // Получить заказы клиента
    @Query("""
        SELECT new com.flora.FlorDTO.OrderSummaryDTO(
            o.id,
            o.cart.id,
            o.totalPrice,
            p.paid,
            o.createdAt,
            o.deliveryAddress
        )
        FROM Order o
        JOIN Payment p ON p.order = o
        WHERE o.cart.client.id = :clientId
        ORDER BY o.id DESC
        """)
    List<OrderSummaryDTO> findOrdersByClientId(
            @Param("clientId") int clientId
    );

    // Получить товары сразу для нескольких заказов
    @Query("""
        SELECT new com.flora.FlorDTO.OrderHistoryItemRowDTO(
            oi.order.id,
            oi.productFlor.id,
            oi.prodName,
            oi.quantity,
            oi.price
        )
        FROM OrderItem oi
        WHERE oi.order.id IN :orderIds
        ORDER BY oi.order.id, oi.id
        """)
    List<OrderHistoryItemRowDTO> findItemsByOrderIds(
            @Param("orderIds") List<Integer> orderIds
    );
}