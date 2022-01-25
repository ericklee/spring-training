package com.hkjc.notificationsvc.repository;

import com.hkjc.notificationsvc.domain.Recipient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataMongoTest(properties = "spring.mongodb.embedded.version=4.0.12")
class RecipientRepositoryTest {

    @Autowired
    RecipientRepository recipientRepository;

    @Test
    void shouldReturnRecipientForAccountId() {
        Recipient recipient = new Recipient("ACC1234567", "Mark", LocalDateTime.now());
        recipientRepository.save(recipient);

        Optional<Recipient> foundRecipient = recipientRepository.findById("ACC1234567");

        Assertions.assertThat(foundRecipient).isPresent();
        Assertions.assertThat(foundRecipient.get()).satisfies(t -> {
            Assertions.assertThat(t.getAccountId()).isEqualTo("ACC1234567");
            Assertions.assertThat(t.getName()).isEqualTo("Mark");
            Assertions.assertThat(t.getCreatedAt()).isNotNull();
        });
    }
}