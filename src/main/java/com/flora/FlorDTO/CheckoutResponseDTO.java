package com.flora.FlorDTO;
import java.math.BigDecimal;

public record CheckoutResponseDTO(
        long orderId,
        long cartId,
        BigDecimal totalPrice,
        String message
) {
}