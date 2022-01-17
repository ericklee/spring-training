package com.hkjc.accountsvc.controller;

import com.hkjc.accountsvc.dto.CreateTransactionRequestDto;
import com.hkjc.accountsvc.service.TransactionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/transactions")
    public ResponseEntity<Void> saveTransaction(@RequestBody CreateTransactionRequestDto createTransactionRequestDto, UriComponentsBuilder uriComponentsBuilder) {
        Long txId = transactionService.saveTransaction(createTransactionRequestDto);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/transactions/{id}").buildAndExpand(txId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
