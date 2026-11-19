package com.bankingsim.controller;

import com.bankingsim.dto.AccountRequest;
import com.bankingsim.dto.AmountRequest;
import com.bankingsim.dto.TransactionResponse;
import com.bankingsim.entity.Account;
import com.bankingsim.service.AccountService;
import com.bankingsim.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Account> openAccount(@Valid @RequestBody AccountRequest request) {
        Account created = accountService.openAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> closeAccount(@PathVariable Long id) {
        accountService.closeAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long id, @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(TransactionResponse.from(transactionService.deposit(id, request.getAmount())));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long id, @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(TransactionResponse.from(transactionService.withdraw(id, request.getAmount())));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable Long id) {
        List<TransactionResponse> history = transactionService.getTransactionHistory(id)
                .stream().map(TransactionResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }
}
