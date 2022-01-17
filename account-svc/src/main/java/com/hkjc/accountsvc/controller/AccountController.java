package com.hkjc.accountsvc.controller;

import com.hkjc.accountsvc.dto.AccountDto;
import com.hkjc.accountsvc.dto.CreateAccountRequestDto;
import com.hkjc.accountsvc.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AccountController {


    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/api/accounts")
    public AccountDto getAccountByCustomerId(@RequestParam String customerId) {
        AccountDto accountDto = accountService.findByCustomerId(customerId);
        return accountDto;
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Void> saveAccount(@RequestBody CreateAccountRequestDto createAccountRequestDto, UriComponentsBuilder uriComponentsBuilder) {
        String accountId = accountService.saveAccount(createAccountRequestDto);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/accounts/{id}").buildAndExpand(accountId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
