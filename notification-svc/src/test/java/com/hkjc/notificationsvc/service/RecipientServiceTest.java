package com.hkjc.notificationsvc.service;

import com.hkjc.notificationsvc.domain.Recipient;
import com.hkjc.notificationsvc.dto.RecipientDto;
import com.hkjc.notificationsvc.exception.RecipientNotFoundException;
import com.hkjc.notificationsvc.mapper.RecipientMapper;
import com.hkjc.notificationsvc.mapper.RecipientMapperImpl;
import com.hkjc.notificationsvc.repository.RecipientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Import(RecipientMapperImpl.class)
public class RecipientServiceTest {

    @Mock
    RecipientRepository recipientRepository;

    @Autowired
    RecipientMapper recipientMapper;

    RecipientService cut;

    @BeforeEach
    void setup() {
        cut = new RecipientService(recipientRepository, recipientMapper);
    }

    @Test
    void shouldReturnRecipientForAccountId() {
        Recipient recipient = new Recipient("ACC1234567", "Mark", LocalDateTime.now());

        Mockito.when(recipientRepository.findById(anyString())).thenReturn(Optional.of(recipient));

        RecipientDto recipientDto = cut.getRecipient("ACC1234567");

        Assertions.assertThat(recipientDto).isNotNull();
        Assertions.assertThat(recipientDto.getId()).isEqualTo(recipient.getAccountId());
        Assertions.assertThat(recipientDto.getName()).isEqualTo(recipient.getName());
        Assertions.assertThat(recipientDto.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenAccountIdNotExists() {
        Mockito.when(recipientRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(RecipientNotFoundException.class).isThrownBy(() -> cut.getRecipient("XXX"));
    }

}
