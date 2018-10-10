package io.blueharvest.bank.service;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.CustomerRepository;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link CustomerService}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-08
 **/
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {
    //region field values
    private static final long ID = 1L;
    private static final String NAME = "John";
    private static final String SURNAME = "Smith";
    private static final String ADDRESS = "Amsterdam";
    private static final String POSTCODE = "1234AB";
    private static final String MODIFIED_POSTCODE = "6789XY"; // Used for updating customer details

    private static final int COUNT_OF_CUSTOMERS = 1;
    //endregion

    @Mock
    private Logger logger;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(ID, NAME, SURNAME, ADDRESS, POSTCODE);
    }

    /**
     * Tests {@link CustomerService#get(long)}
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
     * Tests {@link CustomerService#get(long)} but for negative ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetForNegativeId() {
        customerService.get(-1);
    }

    /**
     * Tests {@link CustomerService#get(long)} but for a nonexistent customer
     */
    @Test
    public void testGetForNonexistentCustomer() {
        when(customerRepository.findById(ID)).thenReturn(null);

        Optional<Customer> desiredCustomerOptional = customerService.get(ID);

        assertThat(desiredCustomerOptional).isEmpty();
    }

    /**
     * Tests {@link CustomerService#getAll()}}
     */
    @Test
    public void testGetAll() {
        when(customerRepository.findAll()).thenReturn(ImmutableList.of(testCustomer));

        List<Customer> customers = customerService.getAll();

        assertThat(customers).isNotEmpty();
        assertThat(customers).hasSize(COUNT_OF_CUSTOMERS);
    }

    /**
     * Tests {@link CustomerService#create(Customer)}}
     */
    @Test
    public void testCreate() {
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        Customer createdCustomer = customerService.create(testCustomer);

        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.getName()).isEqualTo(NAME);
        assertThat(createdCustomer.getSurname()).isEqualTo(SURNAME);
    }

    /**
     * Tests {@link CustomerService#create(Customer)}} but for null customer
     */
    @Test(expected = NullPointerException.class)
    public void testCreateForNullCustomer() {
        customerService.create(null);
    }

    /**
     * Tests {@link CustomerService#update(Customer)}
     */
    @Test
    public void testUpdate() {
        testCustomer.setPostcode(MODIFIED_POSTCODE);
        when(customerRepository.existsById(ID)).thenReturn(true);
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        boolean updateSuccessful = customerService.update(testCustomer);

        assertThat(updateSuccessful).isTrue();
    }

    /**
     * Tests {@link CustomerService#update(Customer)} but for null customer
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateForNullCustomer() {
        customerService.update(null);
    }

    /**
     * Tests {@link CustomerService#update(Customer)} for a nonexistent customer
     */
    @Test
    public void testUpdateForNonexistentCustomer() {
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);
        when(customerRepository.existsById(ID)).thenReturn(false);

        boolean updateSuccessful = customerService.update(testCustomer);

        assertThat(updateSuccessful).isFalse();
    }

    /**
     * Tests {@link CustomerService#delete(long)}
     */
    @Test
    public void testDelete() {
        when(customerRepository.existsById(ID)).thenReturn(true);

        boolean updateSuccessful = customerService.delete(ID);

        assertThat(updateSuccessful).isTrue();
    }

    /**
     * Tests {@link CustomerService#delete(long)} but for negative ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteForNegativeId() {
        customerService.delete(-1);
    }

    /**
     * Tests {@link CustomerService#delete(long)} for a nonexistent customer
     */
    @Test
    public void testDeleteForNonexistentCustomer() {
        when(customerRepository.existsById(ID)).thenReturn(false);

        boolean updateSuccessful = customerService.delete(ID);

        assertThat(updateSuccessful).isFalse();
    }

    /**
     * Tests {@link CustomerService#exists(long)}
     */
    @Test
    public void testExists() {
        when(customerRepository.existsById(ID)).thenReturn(true);

        boolean exists = customerService.exists(ID);

        assertThat(exists).isTrue();
    }

}