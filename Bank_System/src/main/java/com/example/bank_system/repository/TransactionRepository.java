package com.example.bank_system.repository;

import com.example.bank_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOriginatingAccountIdOrResultingAccountId(Long originatingAccountId, Long resultingAccountId);
}
