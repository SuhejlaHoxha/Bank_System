package com.example.bank_system.controller;

import com.example.bank_system.model.Transaction;
import com.example.bank_system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/bank/{bankId}/transfer")
    public Transaction createTransaction(@RequestBody Transaction transaction,
                                         @RequestParam boolean useFlatFee,
                                         @RequestParam Transaction.TransactionType transactionType,
                                         @PathVariable Long bankId) {
        return transactionService.createTransaction(transaction, useFlatFee, bankId, transactionType);
    }

    @PostMapping("/bank/{bankId}/withdraw")
    public Transaction withdraw(@RequestParam Long accountId, @RequestParam double amount, @PathVariable Long bankId) {
        return transactionService.createWithdrawalTransaction(accountId, amount, bankId);
    }

    @PostMapping("/bank/{bankId}/deposit")
    public Transaction deposit(@RequestParam Long accountId, @RequestParam double amount, @PathVariable Long bankId) {
        return transactionService.createDepositTransaction(accountId, amount, bankId);
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionsForAccount(@PathVariable Long accountId) {
        return transactionService.getTransactionsForAccount(accountId);
    }
}
