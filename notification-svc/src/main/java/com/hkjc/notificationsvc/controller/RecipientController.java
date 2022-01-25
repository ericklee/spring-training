package com.hkjc.notificationsvc.controller;

import com.hkjc.notificationsvc.dto.CreateRecipientRequestDto;
import com.hkjc.notificationsvc.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecipientController {

    private final RecipientService recipientService;

    @PostMapping("/api/recipients")
    public ResponseEntity<Void> saveRecipient(@RequestBody CreateRecipientRequestDto createRecipientRequestDto) {
        recipientService.saveRecipient(createRecipientRequestDto);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
