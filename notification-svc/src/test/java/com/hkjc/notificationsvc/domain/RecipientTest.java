package com.hkjc.notificationsvc.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RecipientTest {

    @Test
    void test() {
        Recipient recipient = new Recipient("ACC1234567", "Mark", LocalDateTime.now());
        System.out.println(recipient);
    }
}