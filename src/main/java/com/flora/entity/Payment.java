package com.flora.entity;


import java.math.BigDecimal;

public class Payment {

    private BigDecimal full_cost;
    private boolean paid = false;
    private int order_id;

    public Payment() {

    }

    public static Payment.Builder builder() {
        return new Payment.Builder();
    }


    public static class Builder {

        private BigDecimal full_cost;
        private boolean paid = false;
        private int order_id;


        public Payment.Builder full_cost(BigDecimal full_cost) {
            this.full_cost = full_cost;
            return this;
        }


        public Payment.Builder paid(boolean paid) {
            this.paid = paid;
            return this;
        }

        public Payment.Builder order_id(int order_id) {
            this.order_id = order_id;
            return this;
        }


        public Payment build() {

            Payment payment = new Payment();

            payment.full_cost = this.full_cost;
            payment.paid = this.paid;
            payment.order_id = this.order_id;

            return payment;
        }
    }

    public BigDecimal getFull_cost() {
        return full_cost;
    }

    public void setFull_cost(BigDecimal full_cost) {
        this.full_cost = full_cost;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
