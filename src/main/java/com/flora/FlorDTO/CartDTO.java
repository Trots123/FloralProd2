package com.flora.FlorDTO;

import java.math.BigDecimal;

public class CartDTO {
    private int clientId;
    private int prodId;
    private String prodName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;

    public CartDTO(int clientId, int prodId, String prodName, int quantity, BigDecimal price, BigDecimal total) {
        this.clientId = clientId;
        this.prodId = prodId;
        this.prodName = prodName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int id) {
        this.clientId = clientId;
    }
}
