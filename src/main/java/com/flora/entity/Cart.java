package com.flora.entity;


public class Cart {

    private int id;

    private int user_id;
    private boolean dral_ststus = false;

    public static Cart.Builder builder() {
        return new Cart.Builder();
    }

    public Cart() {
    }

    public static class Builder {

        private int user_id;
        private boolean dral_ststus = false;


        public Cart.Builder user_id(int user_id) {
            this.user_id = user_id;
            return this;
        }


        public Cart.Builder dral_ststus(boolean dral_ststus) {
            this.dral_ststus = dral_ststus;
            return this;
        }


        public Cart build() {

            Cart cartD = new Cart();

            cartD.user_id = this.user_id;
            cartD.dral_ststus = this.dral_ststus;

            return cartD;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isDral_ststus() {
        return dral_ststus;
    }

    public void setDral_ststus(boolean dral_ststus) {
        this.dral_ststus = dral_ststus;
    }
}
