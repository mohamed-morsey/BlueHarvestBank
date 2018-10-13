package io.blueharvest.bank.rest.integration;

import io.blueharvest.bank.dto.AccountDto;
import io.blueharvest.bank.dto.CustomerDto;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

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
import static io.blueharvest.bank.rest.AccountController.ACCOUNT_DTO_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMERS_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMER_DTO_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.TransactionController.TRANSACTIONS_ATTRIBUTE_NAME;
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
    private CustomerDto testCustomerDto;
    private Account testAccount;
    private AccountDto testAccountDto;
    private UriComponentsBuilder builder;
    private ModelMapper mapper = new ModelMapper(); // Mapper for converting between entities and DTOs

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);
        testCustomerDto = new CustomerDto();
        mapper.map(testCustomer, testCustomerDto);

        testAccount = new Account(ACCOUNT_ID, CREDIT);
        testAccountDto = new AccountDto();
        mapper.map(testAccount, testAccountDto);

        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController, accountController, transactionController)
                .setViewResolvers(new StandaloneMvcTestViewResolver()).setValidator(new CustomerValidator())
                .setControllerAdvice(new BankExceptionHandler()).build();
    }

    @After
    public void teardown() {
        customerRepository.deleteAll();
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    /**
     * Tests the following scenario
     * <ul>
     * <li>Get number of customers, accounts and transactions in the system</li>
     * <li>Add customer</li>
     * <li>Get number of customers after add</li>
     * <li>Get number of accounts</li>
     * <li>Add account</li>
     * <li>Get number of accounts after add</li>
     * <li>Get number of transactions after add</li>
     * </ul>
     */
    @Test
    public void testCrudForCustomersAndAccounts() throws Exception {
        // 1- Get the number of customers, accounts and transactions before adding any -> all should be 0

        // Prepare the correct URI to contact the customer controller
        builder = UriComponentsBuilder.fromUriString("/" + CUSTOMERS_CONTEXT_PTAH);
        String uri = builder.toUriString();

        this.mockMvc.perform(get(uri + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(CUSTOMERS_ATTRIBUTE_NAME, hasSize(0)));

        // Prepare the correct URI to contact the account controller
        builder.replacePath("/" + ACCOUNTS_CONTEXT_PTAH);
        uri = builder.toUriString();

        this.mockMvc.perform(get(uri + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ACCOUNTS_ATTRIBUTE_NAME, hasSize(0)));

        // Prepare the correct URI to contact the transaction controller
        builder.replacePath("/" + TRANSACTIONS_CONTEXT_PTAH);
        uri = builder.toUriString();

        this.mockMvc.perform(get(uri + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TRANSACTIONS_ATTRIBUTE_NAME, hasSize(0)));

        // 2- Add a customer
        builder.replacePath("/" + CUSTOMERS_CONTEXT_PTAH);
        uri = builder.toUriString();
        this.mockMvc.perform(post(uri)
                .flashAttr(CUSTOMER_DTO_ATTRIBUTE_NAME, testCustomerDto))
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
                .flashAttr(ACCOUNT_DTO_ATTRIBUTE_NAME, testAccountDto))
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