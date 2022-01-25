package com.hkjc.accountsvc;

import com.hkjc.accountsvc.domain.TransactionType;
import com.hkjc.accountsvc.dto.TransactionDto;
import com.hkjc.accountsvc.service.TransactionService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest(properties = "spring.flyway.enabled=false")
@ActiveProfiles("integration-test")
public class BaseTestClass {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    TransactionService transactionService;

    @BeforeEach
    void shouldReturnTransaction() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountId("ACC1234567");
        transactionDto.setTransactionId(1L);
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setAmount(BigDecimal.valueOf(10));
        transactionDto.setDescription("Token Amount");


        Mockito.when(transactionService.getTransaction(anyLong())).thenReturn(transactionDto);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
}
