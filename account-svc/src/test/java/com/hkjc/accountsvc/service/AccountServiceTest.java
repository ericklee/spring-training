package com.hkjc.accountsvc.service;

import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.dto.AccountDto;
import com.hkjc.accountsvc.domain.AccountType;
import com.hkjc.accountsvc.dto.CreateAccountRequestDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    AccountService cut;

    @BeforeEach
    void before() {
        cut = new AccountService(accountRepository);
    }


    @Test
    @DisplayName("Should Return Account when Customer ID Exists")
    void shouldReturnAccountWhenCustomerIdExists() {

        Account account = new Account();
        account.setAccountId("ACC1234567");
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.ZERO);
        Mockito.when(accountRepository.findByCustomerId("CUST12345")).thenReturn(Optional.of(account));

        AccountDto accountDto = cut.findByCustomerId("CUST12345");

        Assertions.assertThat(accountDto.getAccountId()).isEqualTo("ACC1234567");
        Assertions.assertThat(accountDto.getCustomerId()).isEqualTo("CUST12345");
        Assertions.assertThat(accountDto.getAccountType()).isEqualTo(AccountType.SAVINGS);
        Assertions.assertThat(accountDto.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdNotExists() {
        Mockito.when(accountRepository.findByCustomerId(any())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(() -> cut.findByCustomerId("XXX"));


    }

    @Test
    void shouldReturnAccountIdForSavingNewAccountRequest() {
        Account account = defaultAccount();
        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setCustomerId("CUST12345");
        createAccountRequestDto.setAccountType(AccountType.SAVINGS);

        Mockito.when(accountRepository.save(any())).thenReturn(account);

        String accountId = cut.saveAccount(createAccountRequestDto);

        Assertions.assertThat(accountId).isEqualTo("ACC1234567");

    }

    private Account defaultAccount() {
        Account account = new Account();
        account.setAccountId("ACC1234567");
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.ZERO);
        return account;
    }
}
