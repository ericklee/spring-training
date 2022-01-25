package com.hkjc.notificationsvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.hkjc.accountsvc.dto.TransactionDto;
import com.hkjc.accountsvc.dto.TransactionNotificationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

@Testcontainers
@SpringBootTest
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class TransactionNotificationConsumerIT {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    StreamBridge streamBridge;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance().options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");

    @DynamicPropertySource
    public static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.data.mongodb.url", mongoDBContainer::getReplicaSetUrl);
        registry.add("account-service-url", wireMockExtension::baseUrl);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Queue queue() {
            return new Queue("transaction-notification", false);
        }

        @Bean
        public Exchange exchange() {
            return new TopicExchange("transaction-notification");
        }

        @Bean
        public Binding binding() {
            return BindingBuilder.bind(queue()).to(exchange()).with("#").noargs();
        }
    }

    @Test
    void shouldConsumeMessage() throws JsonProcessingException {

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(1L);
        transactionDto.setAmount(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transactionDto);

        wireMockExtension.stubFor(WireMock.get("/api/transactions/1")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(json)));


        org.springframework.messaging.Message<TransactionNotificationDto> msgSend = MessageBuilder.withPayload(new TransactionNotificationDto(1L)).build();

        streamBridge.send("transaction-notification", msgSend);

        Message message = rabbitTemplate.receive("transaction-notification", 5000);
        Assertions.assertThat(message).isNotNull();
    }
}
