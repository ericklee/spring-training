package com.hkjc.notificationsvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Recipient {

    @Id
    String accountId;
    String name;
    LocalDateTime createdAt;
}
