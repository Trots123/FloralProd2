package com.flora.FlorDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseDTO {
        private long id;
        private String name;
        private String phoneNumber;
        private BigDecimal balance;
}
