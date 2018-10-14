package io.blueharvest.bank.rest;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.dto.CustomerDto;
import io.blueharvest.bank.error.BankExceptionHandler;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.service.CustomerService;
import io.blueharvest.bank.utils.StandaloneMvcTestViewResolver;
import io.blueharvest.bank.validation.CustomerValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
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
import static io.blueharvest.bank.rest.CustomerController.CUSTOMER_DTO_ATTRIBUTE_NAME;
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
    private ModelMapper mapper = new ModelMapper(); // Mapper for converting between entities and DTOs
    private Customer testCustomer;
    private CustomerDto testCustomerDto;

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);
        testCustomerDto = new CustomerDto();

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
                .andExpect(model().attribute(CUSTOMER_DTO_ATTRIBUTE_NAME, equalTo(new CustomerDto())))
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
     * Tests {@link CustomerController#createCustomer(CustomerDto, Errors)} with success
     *
     * @throws Exception
     */
    @Test
    public void testCreateCustomer() throws Exception {
        mapper.map(testCustomer, testCustomerDto);
        when(customerService.create(testCustomer)).thenReturn(testCustomer);

        this.mockMvc.perform(post("/" + CUSTOMERS_CONTEXT_PTAH)
                .flashAttr(CUSTOMER_DTO_ATTRIBUTE_NAME, testCustomerDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + CUSTOMERS_CONTEXT_PTAH));
    }

    /**
     * Tests {@link CustomerController#createCustomer(CustomerDto, Errors)} with failure due to invalid personal details
     *
     * @throws Exception
     */
    @Test
    public void testCreateCustomerWithInvalidPersonalDetails() throws Exception {
        // Set customer name and surname to EMPTY so it would be invalid to add it to the system
        testCustomer.setName(StringUtils.EMPTY);
        testCustomer.setSurname(StringUtils.EMPTY);

        mapper.map(testCustomer, testCustomerDto);
        when(customerService.create(testCustomer)).thenReturn(testCustomer);

        this.mockMvc.perform(post("/" + CUSTOMERS_CONTEXT_PTAH)
                .flashAttr(CUSTOMER_DTO_ATTRIBUTE_NAME, testCustomerDto))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests {@link CustomerController#createCustomer(CustomerDto, Errors)} with failure due to invalid address details
     *
     * @throws Exception
     */
    @Test
    public void testCreateCustomerWithInvalidAddressDetails() throws Exception {
        // Set customer address and postcode to EMPTY so it would be invalid to add it to the system
        testCustomer.setAddress(StringUtils.EMPTY);
        testCustomer.setPostcode(StringUtils.EMPTY);

        mapper.map(testCustomer, testCustomerDto);
        when(customerService.create(testCustomer)).thenReturn(testCustomer);

        this.mockMvc.perform(post("/" + CUSTOMERS_CONTEXT_PTAH)
                .flashAttr(CUSTOMER_DTO_ATTRIBUTE_NAME, testCustomerDto))
                .andExpect(status().isBadRequest());
    }
}