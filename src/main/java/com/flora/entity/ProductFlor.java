package com.flora.entity;

import java.math.BigDecimal;

public class ProductFlor {

    private int id;

    private String name;
    private BigDecimal price;
    private String description;

    public ProductFlor() {

    }

    public ProductFlor(int id , String name, BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static ProductFlor.Builder builder() {
        return new ProductFlor.Builder();
    }


    public static class Builder {
        private String name;
        private BigDecimal price;
        private String description;

        public ProductFlor.Builder name(String name) {
            this.name = name;
            return this;
        }


        public ProductFlor.Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductFlor.Builder description(String description) {
            this.description = description;
            return this;
        }


        public ProductFlor build() {

            ProductFlor productFlor = new ProductFlor();

            productFlor.name = this.name;
            productFlor.price = this.price;
            productFlor.description = this.description;

            return productFlor;
        }

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
