package io.blueharvest.bank.repository;

import io.blueharvest.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Transaction}s
 *
 * @author Mohamed Morsey
 * Date: 2018-10-07
 **/
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findById(Long id);
}
