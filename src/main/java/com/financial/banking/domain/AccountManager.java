package com.financial.banking.domain;

import com.financial.banking.persistence.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business Logic Layer for Account Management
 * Handles all account-related operations
 */
@Service
public class AccountManager {
    
    private final AccountRepository accountRepository;

    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieve all accounts from database
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Find specific account by ID
     */
    public Account getAccountById(String accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    /**
     * Create or update an account
     */
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Remove account from system
     */
    public void removeAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }

    /**
     * Count total accounts
     */
    public long getTotalAccountCount() {
        return accountRepository.count();
    }

    /**
     * Check if account exists
     */
    public boolean accountExists(String accountId) {
        return accountRepository.existsById(accountId);
    }
}
