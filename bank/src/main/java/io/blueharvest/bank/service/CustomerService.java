package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.CustomerRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NOT_FOUND_ERROR;
import static io.blueharvest.bank.constant.Messages.CUSTOMER_NULL_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;

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
    public Customer get(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public boolean create(Customer customer) {
        checkNotNull(customer, CUSTOMER_NULL_ERROR);

        Customer insertedCustomer = customerRepository.save(customer);
        return insertedCustomer != null;
    }

    @Override
    public boolean update(Customer customer) {
        checkNotNull(customer, CUSTOMER_NULL_ERROR);

        if (customerRepository.findById(customer.getId()) == null) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        customerRepository.save(customer);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);

        if (customerRepository.findById(id) == null) {
            logger.warn(CUSTOMER_NOT_FOUND_ERROR);
            return false;
        }

        customerRepository.delete(id);
        return true;
    }
}
