package com.hkjc.notificationsvc.stream;

import com.hkjc.notificationsvc.dto.TransactionNotificationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;

@ExtendWith(OutputCaptureExtension.class)
class TransactionNotificationConsumerTest {

    TransactionNotificationConsumer cut = new TransactionNotificationConsumer();

    @Test
    void shouldPrintMessage(CapturedOutput capturedOutput) {
        TransactionNotificationDto dto = new TransactionNotificationDto(1L);
        cut.accept(MessageBuilder.withPayload(dto).build());

        Assertions.assertThat(capturedOutput.getOut().trim()).isEqualTo("1");
    }
}