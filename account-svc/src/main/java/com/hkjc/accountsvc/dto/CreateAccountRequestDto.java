package com.hkjc.accountsvc.dto;

import com.hkjc.accountsvc.domain.AccountType;

public class CreateAccountRequestDto {

    private String customerId;
    private AccountType accountType;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
