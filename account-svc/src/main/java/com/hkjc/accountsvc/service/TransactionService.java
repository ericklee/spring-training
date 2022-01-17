package com.hkjc.accountsvc.service;

import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.domain.Transaction;
import com.hkjc.accountsvc.domain.TransactionType;
import com.hkjc.accountsvc.dto.CreateTransactionRequestDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.repository.AccountRepository;
import com.hkjc.accountsvc.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Long saveTransaction(CreateTransactionRequestDto createTransactionRequestDto) {
        Optional<Account> account = accountRepository.findById(createTransactionRequestDto.getAccountId());
        Account accountEntity = account.orElseThrow(AccountNotFoundException::new);
        if (createTransactionRequestDto.getTransactionType().equals(TransactionType.DEPOSIT)) {
            accountEntity.setBalance(accountEntity.getBalance().add(createTransactionRequestDto.getAmount()));
        } else {
            accountEntity.setBalance(accountEntity.getBalance().subtract(createTransactionRequestDto.getAmount()));
        }
        accountRepository.save(accountEntity);
        return transactionRepository.save(toTransactionEntity(createTransactionRequestDto)).getTransactionId();
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
