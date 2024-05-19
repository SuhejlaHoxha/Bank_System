package com.example.bank_system.service;

import com.example.bank_system.model.Account;
import com.example.bank_system.model.Bank;
import com.example.bank_system.repository.AccountRepository;
import com.example.bank_system.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankRepository bankRepository;

    public Account createAccount(Account account, Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new RuntimeException("Bank not found"));
        account.setBank(bank);
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
