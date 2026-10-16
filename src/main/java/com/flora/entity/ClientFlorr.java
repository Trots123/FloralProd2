package com.flora.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClientFlorr {
    //
    private int id;
    private String name;
    private String phoneNumber;
    private BigDecimal balance;
    private String passwordHash;
    private LocalDateTime createdAt;

    public ClientFlorr(int id, String name, String phoneNumber, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}