package com.hkjc.accountsvc.dto;

public class TransactionNotificationDto {

    private Long transactionId;

    public TransactionNotificationDto(Long transactionId) {
        this.transactionId = transactionId;
    }


    public Long getTransactionId() {
        return transactionId;
    }


}
