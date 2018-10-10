package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.CustomerRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NULL_ERROR;
import static io.blueharvest.bank.constant.Messages.INVALID_ID_ERROR;

/**
 * A service that supports managing bank customers
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Service
public class CustomerService implements CrudService<Customer> {
    private Logger logger;
    private CustomerRepository customerRepository;

    @Inject
    public CustomerService(
            Logger logger, CustomerRepository customerRepository) {
        this.logger = logger;
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> get(long id) {
        checkArgument(id > 0, INVALID_ID_ERROR);
        return Optional.ofNullable(customerRepository.findById(id));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer create(Customer customer) {
        checkNotNull(customer, CUSTOMER_NULL_ERROR);
        return customerRepository.save(customer);
    }

    @Override
    public boolean update(Customer customer) {
        checkNotNull(customer, CUSTOMER_NULL_ERROR);

        if (!customerRepository.existsById(customer.getId())) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        customerRepository.save(customer);
        return true;
    }

    @Override
    public boolean delete(long id) {
        checkArgument(id > 0, INVALID_ID_ERROR);

        if (!customerRepository.existsById(id)) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        customerRepository.delete(id);
        return true;
    }

    /**
     * Checks if a customer with the given ID exists in the system
     *
     * @param id The ID of the customer
     * @return True if the customer exists, false otherwise
     */
    public boolean exists(long id) {
        return customerRepository.existsById(id);
    }

}
