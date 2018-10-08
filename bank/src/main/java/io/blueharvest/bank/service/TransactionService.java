package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.repository.TransactionRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Messages.TRANSACTION_NULL_ERROR;

/**
 * A service that supports managing transactions
 *
 * @author Mohamed Morsey
 * Date: 2018-10-07
 **/
@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    @Inject
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Returns a specific transaction with the given ID
     * @param id The ID of the transaction
     * @return The transaction if exists, {@link Optional#empty()} otherwise
     */
    public Optional<Transaction> get(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);
        return Optional.ofNullable(transactionRepository.findById(id));
    }

    /**
     * Get a list of all transactions in the system
     * @return List of all transactions in the system
     */
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    /**
     * Returns a list of all transaction of a specific account
     *
     * @param accountId The ID of the {@link Account}
     * @return List of all transactions belonging to the specified account if any exists, otherwise an empty list
     */
    public List<Transaction> getAccountsForCustomer(Long accountId) {
        checkNotNull(accountId, BLANK_INVALID_ID_ERROR);

        Account accountToFind = new Account(accountId); // The account whose transactions should be returned
        return transactionRepository.findByAccount(accountToFind);
    }

    /**
     * Creates a new transaction
     * @param transaction The transaction object to be created
     * @return True if creation was successful, false otherwise
     */
    public boolean create(Transaction transaction) {
        checkNotNull(transaction, TRANSACTION_NULL_ERROR);

        Transaction insertedTransaction = transactionRepository.save(transaction);
        return insertedTransaction != null;
    }

}
