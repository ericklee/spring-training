package com.hkjc.accountsvc.service;

import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.domain.Transaction;
import com.hkjc.accountsvc.domain.TransactionType;
import com.hkjc.accountsvc.dto.CreateTransactionRequestDto;
import com.hkjc.accountsvc.dto.TransactionNotificationDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.repository.AccountRepository;
import com.hkjc.accountsvc.repository.TransactionRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final StreamBridge streamBridge;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, StreamBridge streamBridge) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.streamBridge = streamBridge;
    }

    public Long saveTransaction(CreateTransactionRequestDto createTransactionRequestDto) {
        Optional<Account> account = accountRepository.findById(createTransactionRequestDto.getAccountId());
        Account accountEntity = account.orElseThrow(AccountNotFoundException::new);
        if (createTransactionRequestDto.getTransactionType().equals(TransactionType.DEPOSIT)) {
            accountEntity.setBalance(accountEntity.getBalance().add(createTransactionRequestDto.getAmount()));
        } else {
            accountEntity.setBalance(accountEntity.getBalance().subtract(createTransactionRequestDto.getAmount()));
        }

        Transaction transaction = toTransactionEntity(createTransactionRequestDto);
        Long transactionId = transactionRepository.save(transaction).getTransactionId();
        accountRepository.save(accountEntity);

        streamBridge.send("transaction-notification", MessageBuilder.withPayload(new TransactionNotificationDto((transactionId))).build());

        return transactionId;
    }

    private Transaction toTransactionEntity(CreateTransactionRequestDto createTransactionRequestDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(createTransactionRequestDto.getTransactionType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(createTransactionRequestDto.getDescription());
        transaction.setAmount(createTransactionRequestDto.getAmount());
        transaction.setAccountId(createTransactionRequestDto.getAccountId());
        return transaction;
    }

}
