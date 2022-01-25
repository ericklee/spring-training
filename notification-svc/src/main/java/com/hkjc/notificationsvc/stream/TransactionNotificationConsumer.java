package com.hkjc.notificationsvc.stream;

import com.hkjc.notificationsvc.dto.TransactionNotificationDto;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class TransactionNotificationConsumer implements Consumer<Message<TransactionNotificationDto>> {

    @Override
    public void accept(Message<TransactionNotificationDto> transactionNotificationDtoMessage) {
        System.out.println(transactionNotificationDtoMessage.getPayload().getTransactionId());
    }
}
