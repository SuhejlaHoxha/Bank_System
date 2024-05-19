package com.example.bank_system.controller;

import com.example.bank_system.model.Account;
import com.example.bank_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/bank/{bankId}")
    public Account createAccount(@RequestBody Account account, @PathVariable Long bankId) {
        return accountService.createAccount(account, bankId);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }
}
