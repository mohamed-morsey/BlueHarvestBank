package io.blueharvest.bank.rest;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.service.TransactionService;
import io.blueharvest.bank.validation.AccountValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static io.blueharvest.bank.constant.Fields.ACCOUNT_ID_PARAMETER;
import static io.blueharvest.bank.constant.Fields.CUSTOMER_ID_PARAMETER;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Paths.ACCOUNTS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.TRANSACTIONS_CONTEXT_PTAH;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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

    @GetMapping(name = "getTransactionsForAccount")
    public String getAccount(@NotNull @RequestParam(ACCOUNT_ID_PARAMETER) String accountId,
                             Model model, HttpServletResponse response) {

        // Check if a valid account ID is passed
        if ((StringUtils.isBlank(accountId)) || (!StringUtils.isNumeric(accountId))) {
            logger.warn(BLANK_INVALID_ID_ERROR);
            throw new IllegalArgumentException(BLANK_INVALID_ID_ERROR);
        }

        Long accountIdLong = Long.parseLong(accountId);
        getAccount(accountIdLong); // call this just to make sure the account already exists in the system

        List<Transaction> accounts = transactionService.getAccountsForCustomer(accountIdLong);
        model.addAttribute(TRANSACTIONS_ATTRIBUTE_NAME, accounts);

        return "/" + TRANSACTIONS_CONTEXT_PTAH;
    }

    private Account getAccount(Long accountId) {
        Optional<Account> existingAccountOptional = accountService.get(accountId);
        if (!existingAccountOptional.isPresent()) {
            logger.warn(ACCOUNT_NOT_FOUND_ERROR);
            throw new ObjectNotFoundException(ACCOUNT_NOT_FOUND_ERROR, EMPTY);
        }

        return existingAccountOptional.get();
    }
}
