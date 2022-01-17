package com.hkjc.accountsvc.dto;

import com.hkjc.accountsvc.domain.TransactionType;

import java.math.BigDecimal;

public class CreateTransactionRequestDto {

    private String accountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
