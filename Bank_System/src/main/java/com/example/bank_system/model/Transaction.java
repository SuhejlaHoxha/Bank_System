package com.example.bank_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String reason;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originating_account_id")
    @JsonIgnore
    private Account originatingAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resulting_account_id")
    private Account resultingAccount;

    public Transaction() {}

    public Transaction(double amount, String reason, Account originatingAccount, Account resultingAccount) {
        this.amount = amount;
        this.reason = reason;
        this.originatingAccount = originatingAccount;
        this.resultingAccount = resultingAccount;
    }

    public enum TransactionType {
        TRANSFER,
        WITHDRAWAL,
        DEPOSIT
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Account getOriginatingAccount() {
        return originatingAccount;
    }

    public void setOriginatingAccount(Account originatingAccount) {
        this.originatingAccount = originatingAccount;
    }

    public Account getResultingAccount() {
        return resultingAccount;
    }

    public void setResultingAccount(Account resultingAccount) {
        this.resultingAccount = resultingAccount;
    }
}
