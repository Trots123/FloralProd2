package com.flora.entity;

public class ItemCart {


    private int id;

    private int cart_id;
    private int prod_id;
    private int quantity;

    public ItemCart() {

    }

    public static ItemCart.Builder builder() {
        return new ItemCart.Builder();
    }


    public static class Builder {

        private int cart_id;
        private int prod_id;
        private int quantity;


        public ItemCart.Builder cart_id(int cart_id) {
            this.cart_id = cart_id;
            return this;
        }


        public ItemCart.Builder prod_id(int prod_id) {
            this.prod_id = prod_id;
            return this;
        }


        public ItemCart.Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;

        }

        public ItemCart build() {

            ItemCart itemCart = new ItemCart();

            itemCart.cart_id = this.cart_id;
            itemCart.prod_id = this.prod_id;
            itemCart.quantity = this.quantity;

            return itemCart;
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

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
