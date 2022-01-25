package com.hkjc.notificationsvc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkjc.accountsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.math.BigDecimal;

@AutoConfigureStubRunner(ids = "com.hkjc:account-svc:+:stubs:8082", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
public class AccountServiceContractTest {

    @Autowired
    AccountServiceClient accountServiceClient;

    @Test
    void shouldReturnTransactionForId() throws JsonProcessingException {

        TransactionDto transaction = accountServiceClient.getTransaction(1L);

        Assertions.assertThat(transaction).isNotNull();
        Assertions.assertThat(transaction.getAmount().stripTrailingZeros()).isEqualTo(BigDecimal.TEN.stripTrailingZeros());
    }
}
