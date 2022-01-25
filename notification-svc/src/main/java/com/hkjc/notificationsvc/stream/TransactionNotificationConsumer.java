package com.hkjc.notificationsvc.stream;

import com.hkjc.accountsvc.dto.TransactionNotificationDto;
import com.hkjc.notificationsvc.client.AccountServiceClient;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class TransactionNotificationConsumer implements Consumer<Message<TransactionNotificationDto>> {

    private final AccountServiceClient accountServiceClient;

    public TransactionNotificationConsumer(AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;
    }

    @Override
    public void accept(Message<TransactionNotificationDto> transactionNotificationDtoMessage) {
        Long transactionId = transactionNotificationDtoMessage.getPayload().getTransactionId();
        System.out.println(transactionId);

        accountServiceClient.getTransaction(transactionId);
    }
}
