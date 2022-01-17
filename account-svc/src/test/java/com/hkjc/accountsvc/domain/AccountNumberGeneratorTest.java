package com.hkjc.accountsvc.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountNumberGeneratorTest {

    AccountNumberGenerator cut = new AccountNumberGenerator();

    @Test
    void shouldReturnCorrectAccountIdWhenRequested() {

        String generate = cut.generate(null, null).toString();

        Assertions.assertThat(generate).isNotNull();
        Assertions.assertThat(generate).matches("ACC[0-9]+");

    }
}