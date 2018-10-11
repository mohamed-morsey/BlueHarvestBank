package io.blueharvest.bank.rest;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.error.BankExceptionHandler;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.utils.StandaloneMvcTestViewResolver;
import io.blueharvest.bank.validation.AccountValidator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static io.blueharvest.bank.constant.FieldValues.ACCOUNT_ID;
import static io.blueharvest.bank.constant.FieldValues.ADDRESS;
import static io.blueharvest.bank.constant.FieldValues.CREDIT;
import static io.blueharvest.bank.constant.FieldValues.CUSTOMER_ID;
import static io.blueharvest.bank.constant.FieldValues.NAME;
import static io.blueharvest.bank.constant.FieldValues.POSTCODE;
import static io.blueharvest.bank.constant.FieldValues.SURNAME;
import static io.blueharvest.bank.constant.FieldValues.TRANSACTION_ID;
import static io.blueharvest.bank.constant.Fields.CUSTOMER_ID_PARAMETER;
import static io.blueharvest.bank.constant.Paths.ACCOUNTS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.LIST_CONTEXT_PATH;
import static io.blueharvest.bank.rest.AccountController.ACCOUNTS_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.AccountController.ACCOUNT_ATTRIBUTE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link AccountController}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-11
 **/
@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    private static final String INVALID_CUSTOMER_ID = "NonNumeric";

    @Mock
    private AccountService accountService;
    @Mock
    private CustomerService customerService;
    @Mock
    private Logger logger;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private Customer testCustomer;
    private Account testAccount;
    private Transaction testTransaction;

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);
        testAccount = new Account(ACCOUNT_ID, CREDIT, testCustomer);
        testTransaction = new Transaction(TRANSACTION_ID, CREDIT, testAccount);

        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setViewResolvers(new StandaloneMvcTestViewResolver()).setValidator(new AccountValidator())
                .setControllerAdvice(new BankExceptionHandler()).build();
    }

    /**
     * Tests {@link AccountController#listAccounts(Model)}
     */
    @Test
    public void testListAccounts() throws Exception {
        List<Account> accounts = ImmutableList.of(testAccount);
        when(accountService.getAll()).thenReturn(accounts);

        this.mockMvc.perform(get("/" + ACCOUNTS_CONTEXT_PTAH + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + ACCOUNTS_CONTEXT_PTAH))
                .andExpect(model().attribute(ACCOUNTS_ATTRIBUTE_NAME, equalTo(ImmutableList.of(testAccount))));
    }

    /**
     * Tests {@link AccountController#getAccountsForCustomer(String, Model)}
     */
    @Test
    public void testGetAccountsForCustomer() throws Exception {
        List<Account> accounts = ImmutableList.of(testAccount);
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.of(testCustomer));
        when(accountService.getAccountsForCustomer(CUSTOMER_ID)).thenReturn(accounts);

        this.mockMvc.perform(get("/" + ACCOUNTS_CONTEXT_PTAH)
                .param(CUSTOMER_ID_PARAMETER, String.valueOf(CUSTOMER_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + ACCOUNTS_CONTEXT_PTAH))
                .andExpect(model().attribute(ACCOUNTS_ATTRIBUTE_NAME, equalTo(ImmutableList.of(testAccount))));
    }

    /**
     * Tests {@link AccountController#getAccountsForCustomer(String, Model)} but with an invalid customer ID
     */
    @Test
    public void testGetAccountsForCustomerWithInvalidCustomerId() throws Exception {

        this.mockMvc.perform(get("/" + ACCOUNTS_CONTEXT_PTAH)
                .param(CUSTOMER_ID_PARAMETER, String.valueOf(INVALID_CUSTOMER_ID)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests {@link AccountController#getAccountsForCustomer(String, Model)} but for a nonexistent customer
     */
    @Test
    public void testGetAccountsForCustomerForNonexistentCustomer() throws Exception {
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/" + ACCOUNTS_CONTEXT_PTAH)
                .param(CUSTOMER_ID_PARAMETER, String.valueOf(CUSTOMER_ID)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests {@link AccountController#createAccount(String, Account, Errors)}
     */
    @Test
    public void createAccount() throws Exception {
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.of(testCustomer));
        when(accountService.create(testAccount)).thenReturn(testAccount);

        UriComponentsBuilder targetUrl = UriComponentsBuilder.fromPath("/" + ACCOUNTS_CONTEXT_PTAH);
        targetUrl.queryParam(CUSTOMER_ID_PARAMETER, CUSTOMER_ID);

        this.mockMvc.perform(post("/" + ACCOUNTS_CONTEXT_PTAH)
                .param(CUSTOMER_ID_PARAMETER, String.valueOf(CUSTOMER_ID))
                .flashAttr(ACCOUNT_ATTRIBUTE_NAME, testAccount))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(targetUrl.toUriString()));
    }

    /**
     * Tests {@link AccountController#createAccount(String, Account, Errors)} but with an invalid customer ID
     */
    @Test
    public void createAccountWithInvalidCustomerId() throws Exception {

        this.mockMvc.perform(post("/" + ACCOUNTS_CONTEXT_PTAH)
                .param(CUSTOMER_ID_PARAMETER, String.valueOf(INVALID_CUSTOMER_ID))
                .flashAttr(ACCOUNT_ATTRIBUTE_NAME, testAccount))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests {@link AccountController#createAccount(String, Account, Errors)} but for a nonexistent customer
     */
    @Test
    public void createAccountForNonexistentCustomer() throws Exception {
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/" + ACCOUNTS_CONTEXT_PTAH)
                .param(CUSTOMER_ID_PARAMETER, String.valueOf(CUSTOMER_ID))
                .flashAttr(ACCOUNT_ATTRIBUTE_NAME, testAccount))
                .andExpect(status().isNotFound());
    }
}