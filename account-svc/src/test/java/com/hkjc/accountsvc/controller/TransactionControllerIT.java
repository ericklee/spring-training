package com.hkjc.accountsvc.controller;

import com.hkjc.accountsvc.config.TestData;
import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.domain.AccountType;
import com.hkjc.accountsvc.domain.TransactionType;
import com.hkjc.accountsvc.dto.AccountDto;
import com.hkjc.accountsvc.dto.CreateAccountRequestDto;
import com.hkjc.accountsvc.dto.CreateTransactionRequestDto;
import com.hkjc.accountsvc.repository.AccountRepository;
import com.hkjc.accountsvc.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TransactionControllerIT {

    @Container
    static PostgreSQLContainer container = new PostgreSQLContainer("postgres");

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @DynamicPropertySource
    public static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> container.getJdbcUrl());
        registry.add("spring.datasource.username", () -> container.getUsername());
        registry.add("spring.datasource.password", () -> container.getPassword());
        registry.add("spring.jpa.database-platform", PostgreSQL9Dialect.class::getName);
    }

    @Test
    void shouldSaveTransactionToDB() {

        // Arrange
        Account account = TestData.defaultAccount();
        Account savedAccount = accountRepository.save(account);

        CreateTransactionRequestDto createTransactionRequestDto = new CreateTransactionRequestDto();
        createTransactionRequestDto.setTransactionType(TransactionType.DEPOSIT);
        createTransactionRequestDto.setAccountId(savedAccount.getAccountId());
        createTransactionRequestDto.setAmount(BigDecimal.TEN);
        createTransactionRequestDto.setDescription("Test");

        // Act
        HttpEntity entity = new HttpEntity(createTransactionRequestDto, null);


        ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/transactions",
                HttpMethod.POST, entity, Void.class);

        // Assert
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(exchange.getHeaders().getLocation()).isNotNull();

    }

}
