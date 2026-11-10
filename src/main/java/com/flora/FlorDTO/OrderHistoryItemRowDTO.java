package com.flora.FlorDTO;

import java.math.BigDecimal;

public record OrderHistoryItemRowDTO(
        int orderId,
        int prodId,
        String prodName,
        long quantity,
        BigDecimal price
) {}