package io.blueharvest.bank.repository;

import io.blueharvest.bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Customer}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-04
 **/
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findById(Long id);
    boolean existsById(Long id);
}
