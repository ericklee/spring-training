package com.hkjc.accountsvc.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(MockitoExtension.class)
class FunctionDemoTest {

    @Mock
    StreamBridge streamBridge;

    FunctionDemo cut = new FunctionDemo();

    @Test
    void shouldReturnUpperCase() {

        FunctionDemo.Person person = new FunctionDemo.Person();
        person.setName("Spring");

        Message<FunctionDemo.Person> build = MessageBuilder.withPayload(person).build();

        Assertions.assertThat(cut.process(streamBridge).apply(build)).isEqualTo("SPRING");
    }

    @Test
    void shouldReturnSupply() {

        Assertions.assertThat(cut.supply().get()).isEqualTo("supply");
    }

    @Test
    void shouldConsume(CapturedOutput capturedOutput) {

        cut.consume().accept("CONSUME");


        Assertions.assertThat(capturedOutput.getOut().trim()).isEqualTo("CONSUME");
    }

}