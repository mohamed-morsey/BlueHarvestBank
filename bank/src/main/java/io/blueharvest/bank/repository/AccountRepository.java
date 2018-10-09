package io.blueharvest.bank.repository;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link Account}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(Long id);

    boolean existsById(Long id);

    List<Account> findByCustomer(Customer customer);
}
