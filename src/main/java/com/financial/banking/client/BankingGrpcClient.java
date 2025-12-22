package com.financial.banking.client;

import com.financial.banking.grpc.stubs.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Simple gRPC Client for testing Bank Account Service
 */
public class BankingGrpcClient {
    
    public static void main(String[] args) {
        // Create a channel to connect to the gRPC server
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext() // Disable TLS for development
                .build();
        
        try {
            // Create a blocking stub for synchronous calls
            BankAccountServiceGrpc.BankAccountServiceBlockingStub stub = 
                    BankAccountServiceGrpc.newBlockingStub(channel);
            
            System.out.println("=== Banking gRPC Client Test ===\n");
            
            // Test 1: Create a new account
            System.out.println("1. Creating a new CHECKING account...");
            CreateAccountRequest createRequest = CreateAccountRequest.newBuilder()
                    .setInitialBalance(1000.0)
                    .setCategory(AccountCategory.CHECKING)
                    .setTimestamp(java.time.Instant.now().toString())
                    .build();
            
            CreateAccountResponse createResponse = stub.createAccount(createRequest);
            System.out.println("   Created Account: " + createResponse.getAccount().getAccountId());
            System.out.println("   Balance: $" + createResponse.getAccount().getBalance());
            System.out.println();
            
            // Test 2: Create another account
            System.out.println("2. Creating a new SAVINGS account...");
            CreateAccountRequest createRequest2 = CreateAccountRequest.newBuilder()
                    .setInitialBalance(5000.0)
                    .setCategory(AccountCategory.SAVINGS)
                    .setTimestamp(java.time.Instant.now().toString())
                    .build();
            
            CreateAccountResponse createResponse2 = stub.createAccount(createRequest2);
            System.out.println("   Created Account: " + createResponse2.getAccount().getAccountId());
            System.out.println("   Balance: $" + createResponse2.getAccount().getBalance());
            System.out.println();
            
            // Test 3: List all accounts
            System.out.println("3. Listing all accounts...");
            ListAllAccountsRequest listRequest = ListAllAccountsRequest.newBuilder().build();
            ListAllAccountsResponse listResponse = stub.listAllAccounts(listRequest);
            
            System.out.println("   Total accounts: " + listResponse.getAccountsCount());
            listResponse.getAccountsList().forEach(account -> {
                System.out.println("   - Account: " + account.getAccountId() + 
                                 " | Balance: $" + account.getBalance() + 
                                 " | Type: " + account.getCategory());
            });
            System.out.println();
            
            // Test 4: Find specific account by ID
            System.out.println("4. Finding account by ID...");
            String accountId = createResponse.getAccount().getAccountId();
            FindAccountRequest findRequest = FindAccountRequest.newBuilder()
                    .setAccountId(accountId)
                    .build();
            
            FindAccountResponse findResponse = stub.findAccountById(findRequest);
            BankAccount foundAccount = findResponse.getAccount();
            System.out.println("   Found Account: " + foundAccount.getAccountId());
            System.out.println("   Balance: $" + foundAccount.getBalance());
            System.out.println("   Type: " + foundAccount.getCategory());
            System.out.println();
            
            // Test 5: Get balance statistics
            System.out.println("5. Getting balance statistics...");
            GetBalanceStatsRequest statsRequest = GetBalanceStatsRequest.newBuilder().build();
            GetBalanceStatsResponse statsResponse = stub.getBalanceStatistics(statsRequest);
            
            BalanceStatistics stats = statsResponse.getStatistics();
            System.out.println("   Total Accounts: " + stats.getTotalAccounts());
            System.out.println("   Total Balance: $" + stats.getTotalBalance());
            System.out.println("   Average Balance: $" + stats.getAverageBalance());
            
            System.out.println("\n=== All tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown the channel
            channel.shutdown();
        }
    }
}
