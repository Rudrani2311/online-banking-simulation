package com.bankingsim.service;

import com.bankingsim.dto.AccountRequest;
import com.bankingsim.entity.Account;
import com.bankingsim.entity.Customer;
import com.bankingsim.exception.ResourceNotFoundException;
import com.bankingsim.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
    }

    public Account openAccount(AccountRequest request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        Account account = new Account(request.getAccountType(), request.getInitialBalance(), customer);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateAccount(Long id, AccountRequest request) {
        Account account = getAccountById(id);
        account.setAccountType(request.getAccountType());
        return accountRepository.save(account);
    }

    public void closeAccount(Long id) {
        Account account = getAccountById(id);
        account.setActive(false);
        accountRepository.save(account);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
