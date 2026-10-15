package com.flora.entity;

import java.math.BigDecimal;


public class Order {

    private int id;

    private int cartId;
    private BigDecimal totalPrice;

    public Order() {

    }

    public Order(int id, int cartId, BigDecimal totalPrice) {
        this.id = id;
        this.cartId = cartId;
        this.totalPrice = totalPrice;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
