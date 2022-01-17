package com.hkjc.accountsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkjc.accountsvc.domain.AccountType;
import com.hkjc.accountsvc.domain.TransactionType;
import com.hkjc.accountsvc.dto.AccountDto;
import com.hkjc.accountsvc.dto.CreateAccountRequestDto;
import com.hkjc.accountsvc.dto.CreateTransactionRequestDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.service.AccountService;
import com.hkjc.accountsvc.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturn201AndLocationHeaderWhenTransactionCreated() throws Exception {
        Mockito.when(transactionService.saveTransaction(any())).thenReturn(1L);

        CreateTransactionRequestDto createTransactionRequestDto = new CreateTransactionRequestDto();
        createTransactionRequestDto.setTransactionType(TransactionType.DEPOSIT);
        createTransactionRequestDto.setAccountId("ACC1234567");
        createTransactionRequestDto.setAmount(BigDecimal.TEN);
        createTransactionRequestDto.setDescription("Test");

        String transactionJson = objectMapper.writeValueAsString(createTransactionRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(transactionJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/transactions/1"));
    }
}
