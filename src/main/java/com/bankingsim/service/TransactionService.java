package com.bankingsim.service;

import com.bankingsim.dto.TransferRequest;
import com.bankingsim.entity.Account;
import com.bankingsim.entity.Transaction;
import com.bankingsim.entity.TransactionType;
import com.bankingsim.exception.InsufficientBalanceException;
import com.bankingsim.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountService.getAccountById(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountService.save(account);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setStatus("SUCCESS");
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        Account account = accountService.getAccountById(accountId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in account " + account.getAccountNumber()
                            + ". Available: " + account.getBalance() + ", Requested: " + amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountService.save(account);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setStatus("SUCCESS");
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(TransferRequest request) {
        Account source = accountService.getAccountById(request.getSourceAccountId());
        Account target = accountService.getAccountById(request.getTargetAccountId());
        BigDecimal amount = request.getAmount();

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in source account " + source.getAccountNumber()
                            + ". Available: " + source.getBalance() + ", Requested: " + amount);
        }

        // Atomic balance update: both legs succeed together or the transaction rolls back.
        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
        accountService.save(source);
        accountService.save(target);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setAmount(amount);
        transaction.setSourceAccount(source);
        transaction.setTargetAccount(target);
        transaction.setStatus("SUCCESS");
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findBySourceAccount_IdOrTargetAccount_IdOrderByTimestampDesc(accountId, accountId);
    }
}
