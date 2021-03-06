package io.blueharvest.bank.rest;

import io.blueharvest.bank.dto.AccountDto;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.validation.AccountValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static io.blueharvest.bank.constant.Fields.CUSTOMER_ID_PARAMETER;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Paths.ACCOUNTS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.LIST_CONTEXT_PATH;

/**
 * Controller for {@link Account}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
@Controller
@RequestMapping("/" + ACCOUNTS_CONTEXT_PTAH)
public class AccountController {
    public static final String ACCOUNT_DTO_ATTRIBUTE_NAME = "accountDto";
    public static final String ACCOUNTS_ATTRIBUTE_NAME = "accounts";

    private AccountService accountService;
    private AccountValidator accountValidator;
    private CustomerService customerService;
    private Logger logger;

    private ModelMapper mapper = new ModelMapper(); // Mapper for converting between entities and DTOs

    @Inject
    public AccountController(AccountService accountService, AccountValidator accountValidator,
                             CustomerService customerService, Logger logger) {
        this.accountService = accountService;
        this.accountValidator = accountValidator;
        this.customerService = customerService;
        this.logger = logger;
    }

    @InitBinder("accountDto")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(accountValidator);
    }

    /**
     * Return all accounts in the system
     *
     * @param model
     * @return
     */
    @GetMapping(path = "/" + LIST_CONTEXT_PATH, name = "getAccounts")
    public String listAccounts(Model model) {
        List<Account> accounts = accountService.getAll();
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return "/" + ACCOUNTS_CONTEXT_PTAH;
    }

    /**
     * Return all accounts of a specific customer
     *
     * @param customerId The ID of the customer
     * @param model
     * @return
     */
    @GetMapping(name = "getAccountsForCustomer")
    public String getAccountsForCustomer(@NotNull @RequestParam(CUSTOMER_ID_PARAMETER) String customerId,
                                         Model model) {

        // Check if a valid customer ID is passed
        if ((StringUtils.isBlank(customerId)) || (!StringUtils.isNumeric(customerId))) {
            logger.warn(INVALID_ID_ERROR);
            throw new IllegalArgumentException(INVALID_ID_ERROR);
        }

        // Check if the customer already exists
        long customerIdLong = Long.parseLong(customerId);
        if (!customerService.exists(customerIdLong)) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            throw new ObjectNotFoundException(CUSTOMER_NOT_FOUND_ERROR, StringUtils.EMPTY);
        }

        // This account is initialized to enable setting input fields to default values
        AccountDto initializedAccountDto = new AccountDto();
        initializedAccountDto.setCustomerId(customerIdLong);
        model.addAttribute(ACCOUNT_DTO_ATTRIBUTE_NAME, initializedAccountDto); // Required for initializing the input fields

        List<Account> accounts = accountService.getAccountsForCustomer(customerIdLong);
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, accounts);

        return "/" + ACCOUNTS_CONTEXT_PTAH;
    }


    /**
     * Creates an account and its associated transaction
     *
     * @param customerId The ID of the customer
     * @param accountDto The DTO of account to be created
     * @return
     */
    @PostMapping(name = "createAccount")
    public String createAccount(@NotNull @RequestParam(CUSTOMER_ID_PARAMETER) String customerId,
                                @Validated @ModelAttribute AccountDto accountDto, Errors errors) {

        // Check if a valid customer ID is passed
        if ((StringUtils.isBlank(customerId)) || (!StringUtils.isNumeric(customerId))) {
            throw new IllegalArgumentException(INVALID_ID_ERROR);
        }

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).toString());
        }

        // Get the customer associated with
        Account account = new Account();
        mapper.map(accountDto, account);

        long customerIdLong = Long.parseLong(customerId);
        account.setCustomer(getCustomer(customerIdLong));

        accountService.create(account);

        // Redirect to the same page with the customer ID
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("redirect:/" + ACCOUNTS_CONTEXT_PTAH);
        builder.queryParam(CUSTOMER_ID_PARAMETER, account.getCustomer().getId());

        return builder.build().toUriString();
    }

    private Customer getCustomer(long customerId) {
        Optional<Customer> existingCustomerOptional = customerService.get(customerId);
        if (!existingCustomerOptional.isPresent()) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            throw new ObjectNotFoundException(CUSTOMER_NOT_FOUND_ERROR, StringUtils.EMPTY);
        }

        return existingCustomerOptional.get();
    }
}
