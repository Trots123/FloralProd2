package com.flora.entity;

import java.math.BigDecimal;

public class OrderItem {

    private long id;
    private long orderId;
    private long prodId;
    private String prodName;
    private long quantity;
    private BigDecimal price;

    public OrderItem() {
    }

    public OrderItem(
            long id,
            long orderId,
            long prodId,
            String prodName,
            long quantity,
            BigDecimal price
    ) {
        this.id = id;
        this.orderId = orderId;
        this.prodId = prodId;
        this.prodName = prodName;
        this.quantity = quantity;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
