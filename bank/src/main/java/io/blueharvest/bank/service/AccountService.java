package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NULL_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;

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
    private Logger logger;
    @Inject
    public AccountService(AccountRepository accountRepository, CustomerService customerService, Logger logger) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.logger = logger;
    }

    @Override
    public Optional<Account> get(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);
        return Optional.ofNullable(accountRepository.findById(id));
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public boolean create(Account account) {
        checkNotNull(account, ACCOUNT_NULL_ERROR);

        Optional<Customer> customerOptional = customerService.get(account.getCustomer().getId());
        if(!customerOptional.isPresent()){
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        account.setCustomer(customerOptional.get());
        Account insertedAccount = accountRepository.save(account);
        return insertedAccount != null;
    }

    @Override
    public boolean update(Account account) {
        checkNotNull(account, ACCOUNT_NULL_ERROR);

        if (accountRepository.findById(account.getId()) == null) {
            logger.warn(ACCOUNT_NOT_FOUND_ERROR);
            return false;
        }

        Optional<Customer> customerOptional = customerService.get(account.getCustomer().getId());
        if(!customerOptional.isPresent()){
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        account.setCustomer(customerOptional.get());

        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);

        if (accountRepository.findById(id) == null) {
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
    public List<Account> getAccountsForCustomer(Long customerId) {
        checkNotNull(customerId, BLANK_INVALID_ID_ERROR);

        Customer customerToFind = new Customer(customerId); // The customer whose accounts should be returned
        return accountRepository.findByCustomer(customerToFind);
    }
}
