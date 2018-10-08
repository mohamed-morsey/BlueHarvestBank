package io.blueharvest.bank.rest;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.service.CustomerService;
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

import static io.blueharvest.bank.constant.Fields.CUSTOMER_ID_PARAMETER;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Paths.ACCOUNTS_CONTEXT_PTAH;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Controller for {@link Account}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
@Controller
@RequestMapping("/" + ACCOUNTS_CONTEXT_PTAH)
public class AccountController {
    public static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    public static final String ACCOUNTS_ATTRIBUTE_NAME = "accounts";

    private AccountService accountService;
    private AccountValidator accountValidator;
    private CustomerService customerService;
    private Logger logger;

    @Inject
    public AccountController(AccountService accountService, AccountValidator accountValidator,
                             CustomerService customerService, Logger logger) {
        this.accountService = accountService;
        this.accountValidator = accountValidator;
        this.customerService = customerService;
        this.logger = logger;
    }

    @InitBinder("account")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(accountValidator);
    }

    @GetMapping(path = "/list", name = "getAccounts")
    @ResponseStatus()
    public String getAccounts(Model model) {
        List<Account> accounts = accountService.getAll();
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return "/" + ACCOUNTS_CONTEXT_PTAH;
    }

    @GetMapping(name = "getAccountsForCustomer")
    public String getAccount(@NotNull @RequestParam(CUSTOMER_ID_PARAMETER) String customerId,
                             Model model, HttpServletResponse response) {

        // Check if a valid customer ID is passed
        if ((StringUtils.isBlank(customerId)) || (!StringUtils.isNumeric(customerId))) {
            logger.warn(BLANK_INVALID_ID_ERROR);
            throw new IllegalArgumentException(BLANK_INVALID_ID_ERROR);
        }

        Long customerIdLong = Long.parseLong(customerId);

        // This account is initialized to enable setting input fields to default values
        Account initializedAccount = new Account();
        initializedAccount.setCustomer(getCustomer(customerIdLong));
        model.addAttribute(ACCOUNT_ATTRIBUTE_NAME, initializedAccount); // Required for initializing the input fields

        List<Account> accounts = accountService.getAccountsForCustomer(customerIdLong);
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, accounts);

        return "/" + ACCOUNTS_CONTEXT_PTAH;
    }

    @PostMapping(name = "createAccount")
    public String createAccount(@NotNull @RequestParam(CUSTOMER_ID_PARAMETER) String customerId,
                                @Valid @ModelAttribute Account account, Errors errors) {

        // Check if a valid customer ID is passed
        if ((StringUtils.isBlank(customerId)) || (!StringUtils.isNumeric(customerId))) {
            throw new IllegalArgumentException(BLANK_INVALID_ID_ERROR);
        }

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).toString());
        }

        // Get the customer associated with
        Long customerIdLong = Long.parseLong(customerId);
        account.setCustomer(getCustomer(customerIdLong));

        accountService.create(account);

        // Redirect to the same page with the customer ID
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("redirect:/" + ACCOUNTS_CONTEXT_PTAH);
        builder.queryParam(CUSTOMER_ID_PARAMETER, account.getCustomer().getId());

        return builder.build().toUriString();
    }

    private Customer getCustomer(Long customerId){
        Optional<Customer> existingCustomerOptional = customerService.get(customerId);
        if (!existingCustomerOptional.isPresent()) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            throw new ObjectNotFoundException(CUSTOMER_NOT_FOUND_ERROR, EMPTY);
        }

        return existingCustomerOptional.get();
    }
}
