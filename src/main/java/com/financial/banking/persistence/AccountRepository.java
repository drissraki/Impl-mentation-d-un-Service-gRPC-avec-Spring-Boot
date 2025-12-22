package com.financial.banking.persistence;

import com.financial.banking.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Interface for Account Entity
 * Provides database operations for bank accounts
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    // Additional custom queries can be defined here if needed
}
