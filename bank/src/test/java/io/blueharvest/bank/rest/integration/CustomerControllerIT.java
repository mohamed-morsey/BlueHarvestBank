package io.blueharvest.bank.rest.integration;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.error.BankExceptionHandler;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.AccountRepository;
import io.blueharvest.bank.repository.CustomerRepository;
import io.blueharvest.bank.repository.TransactionRepository;
import io.blueharvest.bank.rest.AccountController;
import io.blueharvest.bank.rest.CustomerController;
import io.blueharvest.bank.rest.TransactionController;
import io.blueharvest.bank.utils.StandaloneMvcTestViewResolver;
import io.blueharvest.bank.validation.CustomerValidator;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

import java.util.List;

import static io.blueharvest.bank.constant.FieldValues.ACCOUNT_ID;
import static io.blueharvest.bank.constant.FieldValues.ADDRESS;
import static io.blueharvest.bank.constant.FieldValues.CREDIT;
import static io.blueharvest.bank.constant.FieldValues.CUSTOMER_ID;
import static io.blueharvest.bank.constant.FieldValues.NAME;
import static io.blueharvest.bank.constant.FieldValues.POSTCODE;
import static io.blueharvest.bank.constant.FieldValues.SURNAME;
import static io.blueharvest.bank.constant.Fields.ACCOUNT_ID_PARAMETER;
import static io.blueharvest.bank.constant.Fields.CUSTOMER_ID_PARAMETER;
import static io.blueharvest.bank.constant.Paths.ACCOUNTS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.CUSTOMERS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.LIST_CONTEXT_PATH;
import static io.blueharvest.bank.constant.Paths.TRANSACTIONS_CONTEXT_PTAH;
import static io.blueharvest.bank.rest.AccountController.ACCOUNTS_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.AccountController.ACCOUNT_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMERS_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMER_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.TransactionController.TRANSACTIONS_ATTRIBUTE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test class for {@link CustomerController} and {@link AccountController} that tests the whole creation scenario
 *
 * @author Mohamed Morsey
 * Date: 2018-10-11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIT {

    private static final ParameterizedTypeReference<String> STRING_RESPONSE_TYPE =
            new ParameterizedTypeReference<String>() {
            };

    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private TransactionRepository transactionRepository;
    @Inject
    private TestRestTemplate restTemplate;
    @Inject
    private CustomerController customerController;
    @Inject
    private AccountController accountController;
    @Inject
    private TransactionController transactionController;

    private MockMvc mockMvc;

    private Customer testCustomer;
    private Account testAccount;
    private UriComponentsBuilder builder;

    private SoftAssertions softly = new SoftAssertions();

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);
        testAccount = new Account(ACCOUNT_ID, CREDIT);

        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController, accountController, transactionController)
                .setViewResolvers(new StandaloneMvcTestViewResolver()).setValidator(new CustomerValidator())
                .setControllerAdvice(new BankExceptionHandler()).build();
    }

    @After
    public void teardown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    /**
     * Tests the following scenario
     * <ul>
     * <li>Create customer</li>
     * <li>Open account for customer</li>
     * <li>Make sure a transaction is created for the account</li>
     * </ul>
     */
    @Test
    public void testCrudForCustomersAndAccounts() throws Exception {
        // Prepare the correct URI to contact the controller
        builder = UriComponentsBuilder.fromUriString("/" + CUSTOMERS_CONTEXT_PTAH);
        String uri = builder.toUriString();

        // 1- Get the number of customers before adding any -> should be 0
        this.mockMvc.perform(get(uri + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(CUSTOMERS_ATTRIBUTE_NAME, hasSize(0)));

        // 2- Add a customer
        this.mockMvc.perform(post(uri)
                .flashAttr(CUSTOMER_ATTRIBUTE_NAME, testCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(uri));

        // 3- Get the number of customers -> should be 1
        this.mockMvc.perform(get(uri + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(CUSTOMERS_ATTRIBUTE_NAME, hasSize(1)));

        // 4- Get the number of accounts before adding any -> should be 0
        builder.replacePath("/" + ACCOUNTS_CONTEXT_PTAH);
        uri = builder.queryParam(CUSTOMER_ID_PARAMETER, String.valueOf(CUSTOMER_ID)).build().toUriString();

        this.mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ACCOUNTS_ATTRIBUTE_NAME, hasSize(0)));

        // 5- Add an account
        this.mockMvc.perform(post(uri)
                .flashAttr(ACCOUNT_ATTRIBUTE_NAME, testAccount))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(uri));

        // 6- Get the number of accounts -> should be 1
        this.mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ACCOUNTS_ATTRIBUTE_NAME, hasSize(1)));

        // 7- Get the number of transactions -> should be 1
        builder.replacePath("/" + TRANSACTIONS_CONTEXT_PTAH);
        builder.replaceQueryParam(CUSTOMER_ID_PARAMETER); // Remove customerId parameter from URL
        uri = builder.queryParam(ACCOUNT_ID_PARAMETER, String.valueOf(ACCOUNT_ID)).build().toUriString();
        this.mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TRANSACTIONS_ATTRIBUTE_NAME, hasSize(1)));

    }
}