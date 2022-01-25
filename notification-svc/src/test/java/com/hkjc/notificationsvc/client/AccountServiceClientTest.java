package com.hkjc.notificationsvc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.hkjc.accountsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
public class AccountServiceClientTest {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance().options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @Autowired
    AccountServiceClient accountServiceClient;

    @DynamicPropertySource
    public static void setup(DynamicPropertyRegistry registry) {
        registry.add("account-service-url", wireMockExtension::baseUrl);
    }

    @Test
    void shouldReturnTransactionForId() throws JsonProcessingException {

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(1L);
        transactionDto.setAmount(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transactionDto);

        wireMockExtension.stubFor(WireMock.get("/api/transactions/1")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(json)));

        TransactionDto transaction = accountServiceClient.getTransaction(1L);

        Assertions.assertThat(transaction).isNotNull();
        Assertions.assertThat(transaction.getAmount().stripTrailingZeros()).isEqualTo(BigDecimal.TEN.stripTrailingZeros());
    }
}
