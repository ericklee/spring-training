package com.hkjc.accountsvc.service;

import com.hkjc.accountsvc.config.TestData;
import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.domain.Transaction;
import com.hkjc.accountsvc.domain.TransactionType;
import com.hkjc.accountsvc.dto.CreateTransactionRequestDto;
import com.hkjc.accountsvc.dto.TransactionNotificationDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import com.hkjc.accountsvc.repository.AccountRepository;
import com.hkjc.accountsvc.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    StreamBridge streamBridge;


    TransactionService cut;

    @BeforeEach
    void before() {
        cut = new TransactionService(transactionRepository, accountRepository, streamBridge);
    }

    @Test
    void shouldCreateTransactionAndUpdateAccountBalance() {
        Transaction transaction = TestData.defaultTransaction();
        Account account = TestData.defaultAccount();

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<Message<TransactionNotificationDto>> transactionNotificationDtoCaptor = ArgumentCaptor.forClass(Message.class);

        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(argumentCaptor.capture())).thenReturn(account);
        Mockito.when(transactionRepository.save(any())).thenReturn(transaction);

        CreateTransactionRequestDto createTransactionRequestDto = new CreateTransactionRequestDto();
        createTransactionRequestDto.setTransactionType(TransactionType.DEPOSIT);
        createTransactionRequestDto.setAccountId("ACC1234567");
        createTransactionRequestDto.setAmount(BigDecimal.TEN);
        createTransactionRequestDto.setDescription("Test");

        Long transactionId = cut.saveTransaction(createTransactionRequestDto);

        Mockito.verify(accountRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Mockito.verify(streamBridge, Mockito.times(1)).send(any(), transactionNotificationDtoCaptor.capture());

        Assertions.assertThat(transactionId).isNotNull();

        Account captureAccount = argumentCaptor.getValue();
        Assertions.assertThat(captureAccount.getBalance().stripTrailingZeros()).isEqualTo(BigDecimal.TEN.stripTrailingZeros());

        Message transactionNotificationDtoCaptorValue = transactionNotificationDtoCaptor.getValue();
        TransactionNotificationDto transactionNotificationDto = (TransactionNotificationDto) transactionNotificationDtoCaptorValue.getPayload();
        Assertions.assertThat(transactionNotificationDto.getTransactionId()).isNotNull();

    }

    @Test
    void shouldThrowExceptionWhenAccountNotExists() {
        CreateTransactionRequestDto createTransactionRequestDto = new CreateTransactionRequestDto();
        createTransactionRequestDto.setTransactionType(TransactionType.DEPOSIT);
        createTransactionRequestDto.setAccountId("ACC1234567");
        createTransactionRequestDto.setAmount(BigDecimal.TEN);
        createTransactionRequestDto.setDescription("Test");

        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(() -> cut.saveTransaction(createTransactionRequestDto));
    }

}
