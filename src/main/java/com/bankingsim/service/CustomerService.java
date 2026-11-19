package com.bankingsim.service;

import com.bankingsim.dto.CustomerRequest;
import com.bankingsim.entity.Account;
import com.bankingsim.entity.Customer;
import com.bankingsim.entity.Transaction;
import com.bankingsim.exception.ResourceNotFoundException;
import com.bankingsim.repository.CustomerRepository;
import com.bankingsim.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Customer createCustomer(CustomerRequest request) {
        Customer customer = new Customer(request.getName(), request.getEmail(), request.getPhone(), request.getAddress());
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, CustomerRequest request) {
        Customer customer = getCustomerById(id);
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        for (Account account : customer.getAccounts()) {
            List<Transaction> txs = transactionRepository
                    .findBySourceAccount_IdOrTargetAccount_IdOrderByTimestampDesc(account.getId(), account.getId());
            transactionRepository.deleteAll(txs);
        }
        customerRepository.delete(customer);
    }
}
