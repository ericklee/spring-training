package com.hkjc.notificationsvc.repository;

import com.hkjc.notificationsvc.domain.Recipient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipientRepository extends MongoRepository<Recipient, String> {
}
