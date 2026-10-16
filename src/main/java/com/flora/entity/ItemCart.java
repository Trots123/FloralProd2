package com.flora.entity;

public class ItemCart {


    private int id;

    private int cartId;
    private int prodId;
    private int quantity;

    public ItemCart() {
    }

    public ItemCart(int id, int cart, int prodId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.prodId = prodId;
        this.quantity = quantity;
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

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
