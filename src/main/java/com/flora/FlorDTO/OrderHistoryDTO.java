package com.flora.FlorDTO;

import java.math.BigDecimal;
import java.util.List;


public record OrderHistoryDTO(
        long orderId,
        long cartId,
        BigDecimal totalPrice,
        boolean paid,
        java.time.LocalDateTime createdAt,
        String deliveryAddress,
        List<OrderItemDTO> items) {
    public record OrderItemDTO(long prodId, String prodName, long quantity, BigDecimal price, BigDecimal total) {
    }

}