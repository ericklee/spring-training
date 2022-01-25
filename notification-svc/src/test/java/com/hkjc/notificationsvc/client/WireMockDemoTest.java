package com.hkjc.notificationsvc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.hkjc.accountsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
public class WireMockDemoTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void testWireMockServer(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(1L);
        transactionDto.setAmount(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(transactionDto);

        WireMock.stubFor(WireMock.get("/api/transactions/123")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(json)));

        String url = wireMockRuntimeInfo.getHttpBaseUrl() + "/api/transactions/123";
        ResponseEntity<TransactionDto> exchange = testRestTemplate.exchange(url, HttpMethod.GET, null, TransactionDto.class);

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(exchange.getBody()).isNotNull();
        Assertions.assertThat(exchange.getBody().getTransactionId()).isEqualTo(1L);
        Assertions.assertThat(exchange.getBody().getAmount().stripTrailingZeros()).isEqualTo(BigDecimal.TEN.stripTrailingZeros());

    }
}
