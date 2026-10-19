package com.flora.FlorDTO;
import java.math.BigDecimal;

public record TopUpRequestDTO(
        BigDecimal amount
) {
}