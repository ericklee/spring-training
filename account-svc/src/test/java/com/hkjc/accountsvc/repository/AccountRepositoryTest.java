package com.hkjc.accountsvc.repository;

import com.hkjc.accountsvc.domain.AccountType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.hkjc.accountsvc.domain.Account;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
class AccountRepositoryTest {

	@Autowired
	TestEntityManager testEntityManager;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Test
	void shouldTestEntityManagerNotBeNull() {
		Assertions.assertThat(testEntityManager).isNotNull();
	}
	
	@Test
	void shouldReturnAccountForExistingCustomerId() {

		Account account = new Account();
		account.setAccountType(AccountType.CURRENT);
		account.setCustomerId("CUST12345");
		account.setBalance(BigDecimal.ZERO);
		testEntityManager.persistAndFlush(account);

		Optional<Account> retrievedAccount = accountRepository.findByCustomerId("CUST12345");

		Assertions.assertThat(retrievedAccount).isPresent();
		Assertions.assertThat(retrievedAccount.get().getAccountId()).isNotNull();
		Assertions.assertThat(retrievedAccount.get().getCustomerId()).isEqualTo("CUST12345");
		Assertions.assertThat(retrievedAccount.get().getAccountType()).isEqualTo(AccountType.CURRENT);
		Assertions.assertThat(retrievedAccount.get().getBalance()).isEqualTo(BigDecimal.ZERO);

	}
}
