package io.blueharvest.bank.service;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.repository.TransactionRepository;
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
 * Test class for {@link TransactionService}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-10
 **/
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
    //region field values
    private static final long ID = 10L;
    private static final Double AMOUNT = 1000.50D;
    private static final long ACCOUNT_ID = 1L;

    private static final int COUNT_OF_TRANSACTIONS = 1;
    //endregion

    @Mock
    private Logger logger;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account testAccount;
    private Transaction testTransaction;

    @Before
    public void setUp() throws Exception {
        testAccount = new Account(ACCOUNT_ID);
        testTransaction = new Transaction(ID, AMOUNT, testAccount);
    }

    /**
     * Tests {@link TransactionService#get(long)}
     */
    @Test
    public void testGet() {
        when(transactionRepository.findById(ID)).thenReturn(testTransaction);

        Optional<Transaction> desiredTransactionOptional = transactionService.get(ID);

        assertThat(desiredTransactionOptional).isPresent();
        assertThat(desiredTransactionOptional).hasValueSatisfying(
                customer -> {
                    assertThat(customer.getId()).isEqualTo(ID);
                    assertThat(customer.getAmount()).isEqualTo(AMOUNT);
                });
    }

    /**
     * Tests {@link TransactionService#get(long)} but for negative ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetForNegativeId() {
        transactionService.get(-1);
    }

    /**
     * Tests {@link TransactionService#get(long)} but for a nonexistent transaction
     */
    @Test
    public void testGetForNonexistentTransaction() {
        when(transactionRepository.findById(ID)).thenReturn(null);

        Optional<Transaction> desiredTransactionOptional = transactionService.get(ID);

        assertThat(desiredTransactionOptional).isEmpty();
    }

    /**
     * Tests {@link TransactionService#getAll()}}
     */
    @Test
    public void testGetAll() {
        when(transactionRepository.findAll()).thenReturn(ImmutableList.of(testTransaction));

        List<Transaction> transactions = transactionService.getAll();

        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(COUNT_OF_TRANSACTIONS);
    }

    /**
     * Tests {@link TransactionService#create(Transaction)}
     */
    @Test
    public void testCreate() {
        when(transactionRepository.save(testTransaction)).thenReturn(testTransaction);

        Transaction createdTransaction = transactionService.create(testTransaction);

        assertThat(createdTransaction).isNotNull();
        assertThat(createdTransaction.getId()).isEqualTo(ID);
        assertThat(createdTransaction.getAmount()).isEqualTo(AMOUNT);
    }

    /**
     * Tests {@link TransactionService#create(Transaction)} but for null transaction
     */
    @Test(expected = NullPointerException.class)
    public void testCreateForNullCustomer() {
        transactionService.create(null);
    }

    /**
     * Tests {@link TransactionService#getTransactionsForAccount(long)}
     */
    @Test
    public void testGetTransactionsForAccount() {
        when(transactionRepository.findByAccount(testAccount)).thenReturn(ImmutableList.of(testTransaction));

        List<Transaction> transactionsForAccount = transactionService.getTransactionsForAccount(ACCOUNT_ID);

        assertThat(transactionsForAccount).isNotEmpty();
        assertThat(transactionsForAccount).hasSize(COUNT_OF_TRANSACTIONS);
        assertThat(transactionsForAccount.get(0)).isEqualTo(testTransaction);
    }

    /**
     * Tests {@link AccountService#getAccountsForCustomer(long)} but with negative account ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetTransactionsForAccountForCustomerWithNegativeCustomerId() {
        transactionService.getTransactionsForAccount(-1);
    }

    /**
     * Tests {@link AccountService#getAccountsForCustomer(long)} but for a account with no transactions
     */
    @Test
    public void testDeleteForCustomerWithNoAccounts() {
        when(transactionRepository.findByAccount(testAccount)).thenReturn(ImmutableList.of());

        List<Transaction> transactionsForAccount = transactionService.getTransactionsForAccount(ACCOUNT_ID);

        assertThat(transactionsForAccount).isEmpty();
    }
}