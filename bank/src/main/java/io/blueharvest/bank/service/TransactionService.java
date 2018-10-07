package io.blueharvest.bank.service;

import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.repository.TransactionRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ID_ERROR;
import static io.blueharvest.bank.constant.Messages.TRANSACTION_NULL_ERROR;

/**
 * A service that supports managing transactions
 *
 * @author Mohamed Morsey
 * Date: 2018-10-07
 **/
@Service
public class TransactionService {
    private Logger logger;
    private TransactionRepository transactionRepository;

    @Inject
    public TransactionService(Logger logger, TransactionRepository transactionRepository) {
        this.logger = logger;
        this.transactionRepository = transactionRepository;
    }

    /*

     */
    public Transaction get(Long id) {
        checkNotNull(id, BLANK_INVALID_ID_ERROR);
        return transactionRepository.findById(id);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public boolean create(Transaction transaction) {
        checkNotNull(transaction, TRANSACTION_NULL_ERROR);

        Transaction insertedTransaction = transactionRepository.save(transaction);
        return insertedTransaction != null;
    }

}
