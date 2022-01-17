package com.hkjc.accountsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkjc.accountsvc.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByCustomerId(String accountID);
}
