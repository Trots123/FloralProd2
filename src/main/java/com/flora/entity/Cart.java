package com.flora.entity;


public class Cart {

    private int id;

    private int userId;
    private boolean dralStstus = false;


    public Cart(int id, int userId, boolean dralStstus) {
        this.id = id;
        this.userId = userId;
        this.dralStstus = dralStstus;
    }

    public static Cart.Builder builder() {
        return new Cart.Builder();
    }

    public Cart() {
    }

    public static class Builder {

        private int userId;
        private boolean dralStstus = false;


        public Cart.Builder userId(int userId) {
            this.userId = userId;
            return this;
        }


        public Cart.Builder dralStstus(boolean dralStstus) {
            this.dralStstus = dralStstus;
            return this;
        }


        public Cart build() {

            Cart cartD = new Cart();

            cartD.userId = this.userId;
            cartD.dralStstus = this.dralStstus;

            return cartD;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isDralStstus() {
        return dralStstus;
    }

    public void setDralStstus(boolean dralStstus) {
        this.dralStstus = dralStstus;
    }
}
