package com.hkjc.accountsvc.config;

import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.domain.AccountType;
import com.hkjc.accountsvc.domain.Transaction;
import com.hkjc.accountsvc.domain.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestData {

    public static Account defaultAccount() {
        Account account = new Account();
        account.setAccountId("ACC1234567");
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    public static Transaction defaultTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAccountId("ACC1234567");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(BigDecimal.TEN);
        transaction.setDescription("Test");
        return transaction;
    }
}
