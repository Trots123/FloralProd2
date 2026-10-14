package com.flora.entity;


import java.math.BigDecimal;

public class Payment {

    private BigDecimal fullCost;
    private boolean paid = false;
    private int orderId;

    public Payment() {

    }

    public Payment(BigDecimal fullCost, boolean paid, int orderId) {
        this.fullCost = fullCost;
        this.paid = paid;
        this.orderId = orderId;
    }

    public static Payment.Builder builder() {
        return new Payment.Builder();
    }


    public static class Builder {

        private BigDecimal fullCost;
        private boolean paid = false;
        private int orderId;


        public Payment.Builder fullCost(BigDecimal fullCost) {
            this.fullCost = fullCost;
            return this;
        }


        public Payment.Builder paid(boolean paid) {
            this.paid = paid;
            return this;
        }

        public Payment.Builder orderId(int orderId) {
            this.orderId = orderId;
            return this;
        }


        public Payment build() {

            Payment payment = new Payment();

            payment.fullCost = this.fullCost;
            payment.paid = this.paid;
            payment.orderId = this.orderId;

            return payment;
        }
    }

    public BigDecimal getFullCost() {
        return fullCost;
    }

    public void setFullCost(BigDecimal fullCost) {
        this.fullCost = fullCost;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int order_id) {
        this.orderId = orderId;
    }
}
