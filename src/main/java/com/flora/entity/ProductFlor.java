package com.flora.entity;

import java.math.BigDecimal;

public class ProductFlor {

    private int id;

    private String name;
    private BigDecimal price;
    private String description;

    private int stockQuantity;

    public ProductFlor() {

    }public ProductFlor(
            int id,
            String name,
            BigDecimal price,
            String description,
            int stockQuantity) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
