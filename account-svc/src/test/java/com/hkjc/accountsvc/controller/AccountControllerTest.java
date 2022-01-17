package com.hkjc.accountsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkjc.accountsvc.dto.AccountDto;
import com.hkjc.accountsvc.domain.AccountType;
import com.hkjc.accountsvc.dto.CreateAccountRequestDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.service.AccountService;
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

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturn200OKForGetAccountByCustomerId() throws Exception {

        AccountDto account = new AccountDto();
        account.setAccountId("ACC1234567");
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.ZERO);

        String accountJson = objectMapper.writeValueAsString(account);

        Mockito.when(accountService.findByCustomerId("CUST12345")).thenReturn(account);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                .queryParam("customerId", "CUST12345")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(accountJson));

    }

    @Test
    void shouldReturn404ForGetAccountByCustomerIdNotExist() throws Exception {
        Mockito.when(accountService.findByCustomerId("DUMMY")).thenThrow(AccountNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                        .queryParam("customerId", "DUMMY")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturn201AndLocationHeaderWhenAccountCreated() throws Exception {
        Mockito.when(accountService.saveAccount(any())).thenReturn("ACC1234567");

        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setAccountType(AccountType.SAVINGS);
        createAccountRequestDto.setCustomerId("CUST12345");

        String accountJson = objectMapper.writeValueAsString(createAccountRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/accounts/ACC1234567"));
    }
}
