package com.flora.entity;


public class Cart {

    private int id;

    private int userId;
    private boolean dralStstus = false;

    public Cart(int id, int userId, boolean dralStstus) {
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
