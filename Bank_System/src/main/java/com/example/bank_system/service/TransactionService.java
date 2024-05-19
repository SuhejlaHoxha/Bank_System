package com.example.bank_system.service;

import com.example.bank_system.model.Account;
import com.example.bank_system.model.Bank;
import com.example.bank_system.model.Transaction;
import com.example.bank_system.repository.AccountRepository;
import com.example.bank_system.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankService bankService;

    @Transactional
    public Transaction createTransaction(Transaction transaction, boolean useFlatFee, Long bankId, Transaction.TransactionType transactionType) {
        Bank bank = bankService.getBankById(bankId);
        double fee = useFlatFee ? bank.getTransactionFlatFeeAmount() : (transaction.getAmount() * bank.getTransactionPercentFeeValue() / 100);

        Account originatingAccount = accountRepository.findById(transaction.getOriginatingAccount().getId())
                .orElseThrow(() -> new RuntimeException("Originating account not found"));
        Account resultingAccount = accountRepository.findById(transaction.getResultingAccount().getId())
                .orElseThrow(() -> new RuntimeException("Resulting account not found"));

        if (originatingAccount.getBalance() < transaction.getAmount() + fee) {
            throw new RuntimeException("Insufficient funds");
        }

        originatingAccount.setBalance(originatingAccount.getBalance() - (transaction.getAmount() + fee));
        resultingAccount.setBalance(resultingAccount.getBalance() + transaction.getAmount());

        bank.setTotalTransactionFeeAmount(bank.getTotalTransactionFeeAmount() + fee);
        bank.setTotalTransferAmount(bank.getTotalTransferAmount() + transaction.getAmount());

        accountRepository.save(originatingAccount);
        accountRepository.save(resultingAccount);
        transaction.setType(transactionType);
        transactionRepository.save(transaction);
        bankService.saveBank(bank);

        return transaction;
    }

    @Transactional
    public Transaction createWithdrawalTransaction(Long accountId, double amount, Long bankId) {
        Bank bank = bankService.getBankById(bankId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        double fee = bank.getTransactionFlatFeeAmount();

        account.setBalance(account.getBalance() - (amount + fee));

        bank.setTotalTransactionFeeAmount(bank.getTotalTransactionFeeAmount() + fee);
        bank.setTotalTransferAmount(bank.getTotalTransferAmount() + amount);
        accountRepository.save(account);
        bankService.saveBank(bank);

        Transaction transaction = new Transaction();
        transaction.setOriginatingAccount(account);
        transaction.setAmount(amount);
        transaction.setType(Transaction.TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public Transaction createDepositTransaction(Long accountId, double amount, Long bankId) {
        Bank bank = bankService.getBankById(bankId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);

        bank.setTotalTransferAmount(bank.getTotalTransferAmount() + amount);

        accountRepository.save(account);
        bankService.saveBank(bank);

        Transaction transaction = new Transaction();
        transaction.setOriginatingAccount(account);
        transaction.setAmount(amount);
        transaction.setType(Transaction.TransactionType.DEPOSIT);
        transactionRepository.save(transaction);

        return transaction;
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByOriginatingAccountIdOrResultingAccountId(accountId, accountId);
    }
}
