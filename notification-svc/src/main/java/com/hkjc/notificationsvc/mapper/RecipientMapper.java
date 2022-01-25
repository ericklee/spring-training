package com.hkjc.notificationsvc.mapper;

import com.hkjc.notificationsvc.domain.Recipient;
import com.hkjc.notificationsvc.dto.RecipientDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RecipientMapper {

    @Mapping(source = "id", target = "accountId")
    @Mapping(target = "createdAt", defaultExpression = "java(java.time.LocalDateTime.now())")
    Recipient toRecipient(RecipientDto recipientDto);

    @Mapping(source = "accountId", target = "id")
    @Mapping(target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    RecipientDto toRecipientDto(Recipient recipient);
}
