package com.flora.FlorDTO;

public class AddToCartRequestDTO {

    private int prodId;
    private int quantity;

    public AddToCartRequestDTO() {
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