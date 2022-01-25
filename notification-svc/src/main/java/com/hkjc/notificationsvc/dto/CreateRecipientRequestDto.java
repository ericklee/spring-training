package com.hkjc.notificationsvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRecipientRequestDto {

    String id;
    String name;
}
