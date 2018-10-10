package io.blueharvest.bank.repository;

import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link Transaction}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-07
 **/
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findById(long id);

    List<Transaction> findByAccount(Account account);
}
