package com.financial.banking.api;

import io.grpc.stub.StreamObserver;
import com.financial.banking.domain.Account;
import com.financial.banking.domain.AccountManager;
import com.financial.banking.grpc.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

/**
 * gRPC Service Endpoint Implementation
 * Handles RPC calls for bank account operations
 * 
 * @author Financial Banking Team
 */
@GrpcService
public class BankAccountGrpcEndpoint extends BankAccountServiceGrpc.BankAccountServiceImplBase {
    
    private final AccountManager accountManager;

    public BankAccountGrpcEndpoint(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * List all accounts in the system
     */
    @Override
    public void listAllAccounts(ListAllAccountsRequest request, 
                                StreamObserver<ListAllAccountsResponse> responseObserver) {
        
        var accounts = accountManager.getAllAccounts().stream()
                .map(account -> BankAccount.newBuilder()
                        .setAccountId(account.getAccountId())
                        .setBalance(account.getBalance())
                        .setCreatedAt(account.getCreatedAt())
                        .setCategory(AccountCategory.valueOf(account.getCategory()))
                        .build())
                .collect(Collectors.toList());

        responseObserver.onNext(ListAllAccountsResponse.newBuilder()
                .addAllAccounts(accounts)
                .build());
        responseObserver.onCompleted();
    }

    /**
     * Find account by unique identifier
     */
    @Override
    public void findAccountById(FindAccountRequest request, 
                                StreamObserver<FindAccountResponse> responseObserver) {
        
        var account = accountManager.getAccountById(request.getAccountId());

        if (account != null) {
            var grpcAccount = BankAccount.newBuilder()
                    .setAccountId(account.getAccountId())
                    .setBalance(account.getBalance())
                    .setCreatedAt(account.getCreatedAt())
                    .setCategory(AccountCategory.valueOf(account.getCategory()))
                    .build();

            responseObserver.onNext(FindAccountResponse.newBuilder()
                    .setAccount(grpcAccount)
                    .build());
        } else {
            responseObserver.onError(
                new RuntimeException("Account not found: " + request.getAccountId())
            );
        }
        responseObserver.onCompleted();
    }

    /**
     * Calculate and return balance statistics
     */
    @Override
    public void getBalanceStatistics(GetBalanceStatsRequest request, 
                                     StreamObserver<GetBalanceStatsResponse> responseObserver) {
        
        var accounts = accountManager.getAllAccounts();
        int totalAccounts = accounts.size();
        double totalBalance = 0.0;

        for (var account : accounts) {
            totalBalance += account.getBalance();
        }

        double averageBalance = totalAccounts > 0 ? totalBalance / totalAccounts : 0.0;

        var statistics = BalanceStatistics.newBuilder()
                .setTotalAccounts(totalAccounts)
                .setTotalBalance(totalBalance)
                .setAverageBalance(averageBalance)
                .build();

        responseObserver.onNext(GetBalanceStatsResponse.newBuilder()
                .setStatistics(statistics)
                .build());
        responseObserver.onCompleted();
    }

    /**
     * Create a new bank account
     */
    @Override
    public void createAccount(CreateAccountRequest request, 
                             StreamObserver<CreateAccountResponse> responseObserver) {
        
        // Create new account entity
        var newAccount = new Account();
        newAccount.setBalance(request.getInitialBalance());
        newAccount.setCreatedAt(request.getTimestamp());
        newAccount.setCategory(request.getCategory().name());

        // Persist to database
        var savedAccount = accountManager.saveAccount(newAccount);

        // Convert to gRPC message
        var grpcAccount = BankAccount.newBuilder()
                .setAccountId(savedAccount.getAccountId())
                .setBalance(savedAccount.getBalance())
                .setCreatedAt(savedAccount.getCreatedAt())
                .setCategory(AccountCategory.valueOf(savedAccount.getCategory()))
                .build();

        responseObserver.onNext(CreateAccountResponse.newBuilder()
                .setAccount(grpcAccount)
                .build());
        responseObserver.onCompleted();
    }
}
