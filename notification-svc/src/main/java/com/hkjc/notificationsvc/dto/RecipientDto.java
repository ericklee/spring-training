package com.hkjc.notificationsvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipientDto {

    String id;
    String name;
    String createdAt;
}