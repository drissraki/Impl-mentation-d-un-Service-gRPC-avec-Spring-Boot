package com.financial.banking.domain;

import jakarta.persistence.*;

/**
 * Account Entity - Represents a bank account in the system
 */
@Entity
@Table(name = "bank_accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private String accountId;
    
    @Column(name = "balance", nullable = false)
    private double balance;
    
    @Column(name = "created_at")
    private String createdAt;
    
    @Column(name = "category")
    private String category;

    // Default Constructor
    public Account() {
    }

    // Parameterized Constructor
    public Account(String accountId, double balance, String createdAt, String category) {
        this.accountId = accountId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.category = category;
    }

    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", balance=" + balance +
                ", createdAt='" + createdAt + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
