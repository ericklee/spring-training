package com.hkjc.notificationsvc.service;

import com.hkjc.notificationsvc.domain.Recipient;
import com.hkjc.notificationsvc.dto.CreateRecipientRequestDto;
import com.hkjc.notificationsvc.dto.RecipientDto;
import com.hkjc.notificationsvc.exception.RecipientNotFoundException;
import com.hkjc.notificationsvc.mapper.RecipientMapper;
import com.hkjc.notificationsvc.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientRepository recipientRepository;
    private final RecipientMapper recipientMapper;

    public RecipientDto getRecipient(String accountId) {
        Optional<Recipient> recipient = recipientRepository.findById(accountId);
        Recipient recipientDoc = recipient.orElseThrow(RecipientNotFoundException::new);
        return recipientMapper.toRecipientDto(recipientDoc);
    }

    public void saveRecipient(CreateRecipientRequestDto createRecipientRequestDto) {
        recipientRepository.save(new Recipient(createRecipientRequestDto.getId(), createRecipientRequestDto.getName(), LocalDateTime.now()));
    }
}