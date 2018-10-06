package io.blueharvest.bank.rest;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.validation.AccountValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import static io.blueharvest.bank.constant.Fields.ID_PARAMETER;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Messages.INVALID_PARAMETER_ERROR;
import static io.blueharvest.bank.rest.AccountController.ACCOUNTS_PATH_SEGMENT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Controller for acounts
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
@Controller
@RequestMapping("/" + ACCOUNTS_PATH_SEGMENT)
public class AccountController {
    public static final String ACCOUNT_PATH_SEGMENT = "account";
    public static final String ACCOUNTS_PATH_SEGMENT = "accounts";
    public static final String ACCOUNT_ATTRIBUTE_NAME = "account";
    public static final String ACCOUNTS_ATTRIBUTE_NAME = "accounts";

    private AccountService accountService;
    private AccountValidator accountValidator;
    private Logger logger;

    @Inject
    public AccountController(AccountService accountService, AccountValidator accountValidator, Logger logger) {
        this.accountService = accountService;
        this.accountValidator = accountValidator;
        this.logger = logger;
    }

    @InitBinder("account")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(accountValidator);
    }

    @GetMapping
    public String init(Model model) {
        List<Account> accounts = accountService.getAll();
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, accounts);
        model.addAttribute(ACCOUNT_ATTRIBUTE_NAME, new Account());
        return ACCOUNTS_PATH_SEGMENT;
    }

    @GetMapping(path = "/list", name = "getAccounts")
    @ResponseStatus()
    public String getAccounts(Model model) {
        List<Account> accounts = accountService.getAll();
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, accounts);
        return ACCOUNTS_PATH_SEGMENT;
    }

    @GetMapping(path = "/{id}", name = "getAccount")
    public String getAccount(@NotNull @PathVariable(ID_PARAMETER) String id, Model model, HttpServletResponse response) {
        if ((StringUtils.isBlank(id)) || (!StringUtils.isNumeric(id))) {
            throw new IllegalArgumentException(BLANK_INVALID_ID_ERROR);
        }

        Account account = accountService.get(Long.parseLong(id));
        model.addAttribute(ACCOUNTS_ATTRIBUTE_NAME, ImmutableList.of(account));
        return ACCOUNTS_PATH_SEGMENT;
    }

    @PostMapping(name = "createAccount")
    public String createAccount(@Valid @ModelAttribute Account account, Errors errors, BindingResult bindingResult) {
        if (errors.hasErrors()) {
            return "/" + ACCOUNTS_PATH_SEGMENT;
        }
        accountService.create(account);
        return "redirect:/" + ACCOUNTS_PATH_SEGMENT;
    }

    /**
     * Handler for {@link IllegalArgumentException} that can be thrown in case of invalid parameter is passed
     *
     * @param exp
     * @param response
     * @throws IOException
     */
    @ExceptionHandler
    private void handleIllegalArgumentException(IllegalArgumentException exp, HttpServletResponse response) throws IOException {
        String errorMessage = INVALID_PARAMETER_ERROR + ": " + exp.getMessage();

        logger.error(errorMessage, exp);
        response.sendError(SC_BAD_REQUEST, errorMessage);
    }
}
