package com.flora.entity;


import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    private BigDecimal fullCost;

    @Column(name = "paid", nullable = false)
    private boolean paid = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;



}
