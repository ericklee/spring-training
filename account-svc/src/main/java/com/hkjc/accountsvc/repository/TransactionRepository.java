package com.hkjc.accountsvc.repository;

import com.hkjc.accountsvc.domain.Account;
import com.hkjc.accountsvc.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
