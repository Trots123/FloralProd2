package com.flora.FlorDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryDTO(
        int orderId,
        int cartId,
        BigDecimal totalPrice,
        boolean paid,
        LocalDateTime createdAt,
        String deliveryAddress
) {}