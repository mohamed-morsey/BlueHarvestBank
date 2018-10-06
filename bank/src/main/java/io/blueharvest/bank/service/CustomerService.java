package io.blueharvest.bank.service;

import com.google.common.base.Preconditions;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.repository.CustomerRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.ID_NULL_ERROR;

/**
 * A service that supports reading bank customers data from database
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
        checkNotNull(id, ID_NULL_ERROR);
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public boolean create(Customer item) {
        Customer insertedCustomer = customerRepository.save(item);
        return insertedCustomer != null;
    }

    @Override
    public boolean update(Customer item) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
