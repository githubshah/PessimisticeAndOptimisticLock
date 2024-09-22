package com.example.pessimisticlockingdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@ToString
public class Account {
    @Id
    private Long id;
    private BigDecimal balance;
    private String balanceSheet = "";

    //@Version

    private Long version; // For optimistic locking
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        this.balanceSheet = this.balanceSheet + ", " + balance;
    }

    public String getBalanceSheet() {
        return balanceSheet;
    }

    public void setBalanceSheet(String balanceSheet) {
        this.balanceSheet = balanceSheet;
    }
}
