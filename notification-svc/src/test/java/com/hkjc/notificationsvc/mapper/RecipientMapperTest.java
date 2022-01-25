package com.hkjc.notificationsvc.mapper;

import com.hkjc.notificationsvc.domain.Recipient;
import com.hkjc.notificationsvc.dto.RecipientDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@Import(RecipientMapperImpl.class)
class RecipientMapperTest {

    @Autowired
    RecipientMapper mapper;

    @Test
    void shouldReturnRecipientFromRecipientDto() {
        RecipientDto recipientDto = new RecipientDto("ACC1234567", "Mark", "2022-01-01T01:01:01");
        Recipient recipient = mapper.toRecipient(recipientDto);

        Assertions.assertThat(recipient).isNotNull();
        Assertions.assertThat(recipient.getAccountId()).isEqualTo(recipientDto.getId());
        Assertions.assertThat(recipient.getName()).isEqualTo(recipientDto.getName());
        Assertions.assertThat(recipient.getCreatedAt()).isEqualTo(LocalDateTime.of(2022, 1, 1, 1, 1, 1));

    }

    @Test
    void shouldReturnRecipientDtoFromRecipient() {
        Recipient recipient = new Recipient("ACC1234567", "Mark", LocalDateTime.of(2022, 1, 1, 1, 1, 1));
        RecipientDto recipientDto = mapper.toRecipientDto(recipient);

        Assertions.assertThat(recipientDto).isNotNull();
        Assertions.assertThat(recipientDto.getId()).isEqualTo(recipient.getAccountId());
        Assertions.assertThat(recipientDto.getName()).isEqualTo(recipient.getName());
        Assertions.assertThat(recipientDto.getCreatedAt()).isEqualTo("2022-01-01 01:01:01");

    }

    @Test
    void shouldSetDefaultCreatedAtWhenNoValue() {
        RecipientDto recipientDto = new RecipientDto("ACC1234567", "David", null);
        Recipient recipient = mapper.toRecipient(recipientDto);

        Assertions.assertThat(recipient).isNotNull();
        Assertions.assertThat(recipient.getAccountId()).isEqualTo(recipientDto.getId());
        Assertions.assertThat(recipient.getName()).isEqualTo(recipientDto.getName());
        Assertions.assertThat(recipient.getCreatedAt()).isNotNull();

    }

}