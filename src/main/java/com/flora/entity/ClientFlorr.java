package com.flora.entity;


import java.math.BigDecimal;

public class ClientFlorr {

    private int id;
    private String name;
    private String phoneNumber;
    private BigDecimal balance;

    public ClientFlorr(int id, String name, String phoneNumber, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    public ClientFlorr() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}