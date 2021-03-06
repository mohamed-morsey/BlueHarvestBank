package io.blueharvest.bank.rest;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static io.blueharvest.bank.constant.Fields.ACCOUNT_ID_PARAMETER;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Paths.LIST_CONTEXT_PATH;
import static io.blueharvest.bank.constant.Paths.TRANSACTIONS_CONTEXT_PTAH;

/**
 * Controller for {@link Transaction}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-08
 **/
@Controller
@RequestMapping("/" + TRANSACTIONS_CONTEXT_PTAH)
public class TransactionController {
    public static final String TRANSACTIONS_ATTRIBUTE_NAME = "transactions";

    private TransactionService transactionService;
    private AccountService accountService;
    private Logger logger;

    @Inject
    public TransactionController(TransactionService transactionService, AccountService accountService,
                                 Logger logger) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.logger = logger;
    }

    /**
     * Return all transactions in the system
     *
     * @param model
     * @return
     */
    @GetMapping(path = "/" + LIST_CONTEXT_PATH, name = "getTransactions")
    public String listTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAll();
        model.addAttribute(TRANSACTIONS_ATTRIBUTE_NAME, transactions);
        return "/" + TRANSACTIONS_CONTEXT_PTAH;
    }

    /**
     * Return all transactions of a specific account
     *
     * @param model
     * @return
     */
    @GetMapping(name = "getTransactionsForAccount")
    public String getTransactionForAccount(@NotNull @RequestParam(ACCOUNT_ID_PARAMETER) String accountId, Model model) {

        // Check if a valid account ID is passed
        if ((StringUtils.isBlank(accountId)) || (!StringUtils.isNumeric(accountId))) {
            logger.warn(INVALID_ID_ERROR);
            throw new IllegalArgumentException(INVALID_ID_ERROR);
        }

        long accountIdLong = Long.parseLong(accountId);
        getAccount(accountIdLong); // call this just to make sure the account already exists in the system

        List<Transaction> accounts = transactionService.getTransactionsForAccount(accountIdLong);
        model.addAttribute(TRANSACTIONS_ATTRIBUTE_NAME, accounts);

        return "/" + TRANSACTIONS_CONTEXT_PTAH;
    }

    private Account getAccount(long accountId) {
        Optional<Account> existingAccountOptional = accountService.get(accountId);
        if (!existingAccountOptional.isPresent()) {
            logger.warn(ACCOUNT_NOT_FOUND_ERROR);
            throw new ObjectNotFoundException(ACCOUNT_NOT_FOUND_ERROR, StringUtils.EMPTY);
        }

        return existingAccountOptional.get();
    }
}
