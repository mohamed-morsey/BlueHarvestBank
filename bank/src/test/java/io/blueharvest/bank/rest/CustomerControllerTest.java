package io.blueharvest.bank.rest;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.error.BankExceptionHandler;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.utils.StandaloneMvcTestViewResolver;
import io.blueharvest.bank.validation.CustomerValidator;
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

import java.util.List;

import static io.blueharvest.bank.constant.FieldValues.ADDRESS;
import static io.blueharvest.bank.constant.FieldValues.CUSTOMER_ID;
import static io.blueharvest.bank.constant.FieldValues.NAME;
import static io.blueharvest.bank.constant.FieldValues.POSTCODE;
import static io.blueharvest.bank.constant.FieldValues.SURNAME;
import static io.blueharvest.bank.constant.Paths.CUSTOMERS_CONTEXT_PTAH;
import static io.blueharvest.bank.constant.Paths.LIST_CONTEXT_PATH;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMERS_ATTRIBUTE_NAME;
import static io.blueharvest.bank.rest.CustomerController.CUSTOMER_ATTRIBUTE_NAME;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link CustomerController}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-10
 **/
@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private Customer testCustomer;

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);

        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setViewResolvers(new StandaloneMvcTestViewResolver()).setValidator(new CustomerValidator())
                .setControllerAdvice(new BankExceptionHandler()).build();
    }

    /**
     * Tests {@link CustomerController#init(Model)}
     *
     * @throws Exception
     */
    @Test
    public void testInit() throws Exception {
        List<Customer> customers = ImmutableList.of(testCustomer);
        when(customerService.getAll()).thenReturn(customers);

        this.mockMvc.perform(get("/" + CUSTOMERS_CONTEXT_PTAH))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + CUSTOMERS_CONTEXT_PTAH))
                .andExpect(model().attribute(CUSTOMER_ATTRIBUTE_NAME, equalTo(new Customer())))
                .andExpect(model().attribute(CUSTOMERS_ATTRIBUTE_NAME, equalTo(ImmutableList.of(testCustomer))));
    }


    /**
     * Tests {@link CustomerController#listCustomers(Model)}
     *
     * @throws Exception
     */
    @Test
    public void testListCustomers() throws Exception {
        List<Customer> customers = ImmutableList.of(testCustomer);
        when(customerService.getAll()).thenReturn(customers);

        this.mockMvc.perform(get("/" + CUSTOMERS_CONTEXT_PTAH + "/" + LIST_CONTEXT_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + CUSTOMERS_CONTEXT_PTAH))
                .andExpect(model().attribute(CUSTOMERS_ATTRIBUTE_NAME, equalTo(ImmutableList.of(testCustomer))));
    }

    /**
     * Tests {@link CustomerController#createCustomer(Customer, Errors)} with success
     *
     * @throws Exception
     */
    @Test
    public void testCreateCustomer() throws Exception {
        when(customerService.create(testCustomer)).thenReturn(testCustomer);

        this.mockMvc.perform(post("/" + CUSTOMERS_CONTEXT_PTAH)
                .flashAttr(CUSTOMER_ATTRIBUTE_NAME, testCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + CUSTOMERS_CONTEXT_PTAH));
    }

    /**
     * Tests {@link CustomerController#createCustomer(Customer, Errors)} with failure due to invalid customer details
     *
     * @throws Exception
     */
    @Test
    public void testCreateCustomerWithInvalidCustomerDetails() throws Exception {
        testCustomer.setName(EMPTY); // Set customer name to EMPTY so it would be invalid to add it to the system
        when(customerService.create(testCustomer)).thenReturn(testCustomer);

        this.mockMvc.perform(post("/" + CUSTOMERS_CONTEXT_PTAH)
                .flashAttr(CUSTOMER_ATTRIBUTE_NAME, testCustomer))
                .andExpect(status().isBadRequest());
    }
}