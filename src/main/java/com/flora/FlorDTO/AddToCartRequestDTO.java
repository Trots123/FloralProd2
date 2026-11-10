package com.flora.FlorDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AddToCartRequestDTO {
    private int prodId;
    private int quantity;
}