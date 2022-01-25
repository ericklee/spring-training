package com.hkjc.notificationsvc.stream;

import com.hkjc.accountsvc.dto.TransactionNotificationDto;
import com.hkjc.notificationsvc.client.AccountServiceClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;

@ExtendWith(OutputCaptureExtension.class)
class TransactionNotificationConsumerTest {

    AccountServiceClient accountServiceClient = Mockito.mock(AccountServiceClient.class);

    TransactionNotificationConsumer cut = new TransactionNotificationConsumer(accountServiceClient);

    @Test
    void shouldPrintMessage(CapturedOutput capturedOutput) {
        TransactionNotificationDto dto = new TransactionNotificationDto(1L);
        cut.accept(MessageBuilder.withPayload(dto).build());

        Assertions.assertThat(capturedOutput.getOut().trim()).isEqualTo("1");

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(accountServiceClient).getTransaction(argumentCaptor.capture());

        Assertions.assertThat(argumentCaptor.getValue()).isEqualTo(dto.getTransactionId());
    }
}