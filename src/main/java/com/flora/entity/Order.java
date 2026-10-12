package com.flora.entity;

import java.math.BigDecimal;


public class Order {

    private int id;

    private int cart_id;
    private BigDecimal total_price;

    public Order() {

    }

    public static Order.Builder builder() {
        return new Order.Builder();
    }


    public static class Builder {

        private int cart_id;
        private BigDecimal total_price;


        public Order.Builder cart_id(int cart_id) {
            this.cart_id = cart_id;
            return this;
        }


        public Order.Builder total_price(BigDecimal total_price) {
            this.total_price = total_price;
            return this;
        }


        public Order build() {

            Order order = new Order();

            order.cart_id = this.cart_id;
            order.total_price = this.total_price;

            return order;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }
}
