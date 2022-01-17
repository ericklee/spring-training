package com.hkjc.accountsvc.service;

import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.dto.AccountDto;
import com.hkjc.accountsvc.dto.CreateAccountRequestDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDto findByCustomerId(String customerId) {
        Optional<Account> account = accountRepository.findByCustomerId(customerId);
        Account accountEntity = account.orElseThrow(AccountNotFoundException::new);
        return toAccountDto(accountEntity);
    }

    public String saveAccount(CreateAccountRequestDto createAccountRequestDto) {
        return accountRepository.save(toAccountEntity(createAccountRequestDto)).getAccountId();
    }

    private AccountDto toAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(account.getAccountId());
        accountDto.setCustomerId(account.getCustomerId());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }

    private Account toAccountEntity(CreateAccountRequestDto createAccountRequestDto) {
        Account account = new Account();
        account.setCustomerId(createAccountRequestDto.getCustomerId());
        account.setAccountType(createAccountRequestDto.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

}
