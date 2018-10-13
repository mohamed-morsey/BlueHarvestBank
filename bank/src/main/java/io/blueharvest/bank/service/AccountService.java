package io.blueharvest.bank.service;

import io.blueharvest.bank.error.TransactionalOperationException;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_CREATION_FAILED_ERROR;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NULL_ERROR;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.INVALID_ID_ERROR;

/**
 * A service that supports managing bank accounts
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Service
public class AccountService implements CrudService<Account> {
    private AccountRepository accountRepository;
    private CustomerService customerService;
    private TransactionService transactionService;
    private Logger logger;

    @Inject
    public AccountService(AccountRepository accountRepository, CustomerService customerService,
                          TransactionService transactionService, Logger logger) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.transactionService = transactionService;
        this.logger = logger;
    }

    @Override
    public Optional<Account> get(long id) {
        checkArgument(id > 0, INVALID_ID_ERROR);

        return Optional.ofNullable(accountRepository.findById(id));
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = TransactionalOperationException.class)
    public Account create(Account account) {
        checkNotNull(account, ACCOUNT_NULL_ERROR);

        boolean customerExists = customerService.exists(account.getCustomer().getId());
        if (!customerExists) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            throw new TransactionalOperationException(CUSTOMER_NOT_FOUND_ERROR);
        }

        return createAccountWithTransaction(account);
    }

    @Override
    public boolean update(Account account) {
        checkNotNull(account, ACCOUNT_NULL_ERROR);

        if (!accountRepository.existsById(account.getId())) {
            logger.warn(ACCOUNT_NOT_FOUND_ERROR);
            return false;
        }

        Optional<Customer> customerOptional = customerService.get(account.getCustomer().getId());
        if (!customerOptional.isPresent()) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        account.setCustomer(customerOptional.get());
        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean delete(long id) {
        checkArgument(id > 0, INVALID_ID_ERROR);

        if (!accountRepository.existsById(id)) {
            logger.warn(ACCOUNT_NOT_FOUND_ERROR);
            return false;
        }

        accountRepository.delete(id);
        return true;
    }

    /**
     * Returns a list of all accounts of a specific customer
     *
     * @param customerId The ID of the {@link Customer}
     * @return List of all accounts belonging to the specified customer if any exists, otherwise an empty list
     */
    public List<Account> getAccountsForCustomer(long customerId) {
        checkArgument(customerId > 0, INVALID_ID_ERROR);

        Customer customerToFind = new Customer(customerId); // The customer whose accounts should be returned
        return accountRepository.findByCustomer(customerToFind);
    }

    /**
     * Creates an account with the specified details and creates a transaction associated with it
     *
     * @param account The account to be created
     */
    private Account createAccountWithTransaction(Account account) {
        try {
            Account insertedAccount = accountRepository.save(account);

            // Create a transaction associated with that account with the initail credit
            Transaction initialTransaction = new Transaction();
            initialTransaction.setAccount(insertedAccount);
            initialTransaction.setAmount(account.getCredit());

            // Store the transaction
            transactionService.create(initialTransaction);
            return insertedAccount;
        } catch (DataAccessException exp) { // This exception is thrown in case of any of the save operations fails
            logger.warn(ACCOUNT_CREATION_FAILED_ERROR);
            throw new TransactionalOperationException(ACCOUNT_CREATION_FAILED_ERROR, exp);
        }
    }
}
