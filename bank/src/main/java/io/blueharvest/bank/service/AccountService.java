package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NULL_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;

/**
 * A service that supports managing bank accounts
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Service
public class AccountService implements CrudService<Account> {
    private Logger logger;
    private AccountRepository accountRepository;

    @Inject
    public AccountService(
            Logger logger, AccountRepository accountRepository) {
        this.logger = logger;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account get(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public boolean create(Account account) {
        checkNotNull(account, ACCOUNT_NULL_ERROR);

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
}
