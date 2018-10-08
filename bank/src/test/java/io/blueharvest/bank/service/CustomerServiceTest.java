package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.CustomerRepository;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link CustomerService}
 *
 * @author Mohamed Morsey (mohamedmorsey@semmtech.nl)
 * Date: 2018-10-08
 **/
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {
    //region field values
    private static final Long ID = 1L;
    private static final String NAME = "John";
    private static final String SURNAME = "Smith";
    private static final String ADDRESS = "Amsterdam";
    private static final String POSTCODE = "1234AB";
    //endregion

    @Mock private Logger logger;
    @Mock private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(ID, NAME, SURNAME, ADDRESS, POSTCODE);
    }

    /**
     * Tests {@link CustomerService#get(Long)}
     */
    @Test
    public void testGet() {
        when(customerRepository.findById(ID)).thenReturn(testCustomer);

        Optional<Customer> desiredCustomerOptional = customerService.get(ID);

        assertThat(desiredCustomerOptional).isPresent();
        assertThat(desiredCustomerOptional).hasValueSatisfying(
                customer -> {
                    assertThat(customer.getName()).isEqualTo(NAME);
                    assertThat(customer.getSurname()).isEqualTo(SURNAME);
                });
    }

    /**
     * Tests {@link CustomerService#get(Long)} but for a nonexistent one
     */
    @Test
    public void testGetForNonexistentCustomer() {
        when(customerRepository.findById(ID)).thenReturn(null);

        Optional<Customer> desiredCustomerOptional = customerService.get(ID);

        assertThat(desiredCustomerOptional).isEmpty();
    }

    /**
     * Tests {@link CustomerService#get(Long)} but with NULL ID
     */
    @Test(expected = NullPointerException.class)
    public void testGetForNullId() {
        customerService.get(null);
    }

    @Test
    public void getAll() {
    }

    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}