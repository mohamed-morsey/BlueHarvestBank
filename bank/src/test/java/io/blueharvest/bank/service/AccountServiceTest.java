package io.blueharvest.bank.service;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.error.TransactionalOperationException;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Customer;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static io.blueharvest.bank.constant.FieldValues.ACCOUNT_ID;
import static io.blueharvest.bank.constant.FieldValues.ADDRESS;
import static io.blueharvest.bank.constant.FieldValues.COUNT_OF_ACCOUNTS;
import static io.blueharvest.bank.constant.FieldValues.CREDIT;
import static io.blueharvest.bank.constant.FieldValues.CUSTOMER_ID;
import static io.blueharvest.bank.constant.FieldValues.MODIFIED_CREDIT;
import static io.blueharvest.bank.constant.FieldValues.NAME;
import static io.blueharvest.bank.constant.FieldValues.POSTCODE;
import static io.blueharvest.bank.constant.FieldValues.SURNAME;
import static io.blueharvest.bank.constant.FieldValues.TRANSACTION_ID;
import static io.blueharvest.bank.constant.Messages.ACCOUNT_CREATION_FAILED_ERROR;
import static io.blueharvest.bank.constant.Messages.TRANSACTION_CREATION_FAILED_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link AccountService}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-09
 **/
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
    @Mock
    private Logger logger;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountService accountService;

    private Customer testCustomer;
    private Account testAccount;
    private Transaction testTransaction;

    @Before
    public void setUp() throws Exception {
        testCustomer = new Customer(CUSTOMER_ID, NAME, SURNAME, ADDRESS, POSTCODE);
        testAccount = new Account(ACCOUNT_ID, CREDIT, testCustomer);
        testTransaction = new Transaction(TRANSACTION_ID, CREDIT, testAccount);
    }

    /**
     * Tests {@link AccountService#get(long)}
     */
    @Test
    public void testGet() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(testAccount);

        Optional<Account> desiredAccountOptional = accountService.get(ACCOUNT_ID);

        assertThat(desiredAccountOptional).isPresent();
        assertThat(desiredAccountOptional).hasValueSatisfying(
                account -> {
                    assertThat(account.getId()).isEqualTo(ACCOUNT_ID);
                    assertThat(account.getCredit()).isEqualTo(CREDIT);
                });
    }

    /**
     * Tests {@link AccountService#get(long)} but for negative ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetForNegativeId() {
        accountService.get(-1);
    }

    /**
     * Tests {@link AccountService#get(long)} but for a nonexistent account
     */
    @Test
    public void testGetForNonexistentAccount() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(null);

        Optional<Account> desiredAccountOptional = accountService.get(ACCOUNT_ID);

        assertThat(desiredAccountOptional).isEmpty();
    }

    /**
     * Tests {@link AccountService#getAll()}}
     */
    @Test
    public void testGetAll() {
        when(accountRepository.findAll()).thenReturn(ImmutableList.of(testAccount));

        List<Account> accounts = accountService.getAll();

        assertThat(accounts).isNotEmpty();
        assertThat(accounts).hasSize(COUNT_OF_ACCOUNTS);
    }

    /**
     * Tests {@link AccountService#create(Account)}
     */
    @Test
    public void testCreate() {
        when(customerService.exists(CUSTOMER_ID)).thenReturn(true);
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        when(transactionService.create(any(Transaction.class))).thenReturn(testTransaction);

        Account createdAccount = accountService.create(testAccount);

        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(createdAccount.getCredit()).isEqualTo(CREDIT);
    }

    /**
     * Tests {@link AccountService#create(Account)} but for null account
     */
    @Test(expected = NullPointerException.class)
    public void testCreateForNullAccount() {
        accountService.create(null);
    }

    /**
     * Tests {@link AccountService#create(Account)} but for a nonexistent customer
     */
    @Test(expected = TransactionalOperationException.class)
    public void testCreateForNonexistentCustomer() {
        when(customerService.exists(CUSTOMER_ID)).thenReturn(false);

        accountService.create(testAccount);
    }

    /**
     * Tests {@link AccountService#create(Account)} but when the transaction fails due to failure to create the account
     * in the database
     */
    @Test(expected = TransactionalOperationException.class)
    public void testCreateForTransactionalFailureWithAccountCreationFailure() {
        when(customerService.exists(CUSTOMER_ID)).thenReturn(true);
        when(accountRepository.save(testAccount)).
                thenThrow(new EmptyResultDataAccessException(ACCOUNT_CREATION_FAILED_ERROR, 1));

        accountService.create(testAccount);
    }

    /**
     * Tests {@link AccountService#create(Account)} but when the transaction fails due to failure to create the transaction
     * associated with the account in the database
     */
    @Test(expected = TransactionalOperationException.class)
    public void testCreateForTransactionalFailureWithTransactionCreationFailure() {
        when(customerService.exists(CUSTOMER_ID)).thenReturn(true);
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        when(transactionService.create(any(Transaction.class)))
                .thenThrow(new EmptyResultDataAccessException(TRANSACTION_CREATION_FAILED_ERROR, 1));

        accountService.create(testAccount);
    }

    /**
     * Tests {@link AccountService#update(Account)}
     */
    @Test
    public void testUpdate() {
        testAccount.setCredit(MODIFIED_CREDIT);
        when(accountRepository.existsById(ACCOUNT_ID)).thenReturn(true);
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.of(testCustomer));
        when(accountRepository.save(testAccount)).thenReturn(testAccount);

        boolean updateSuccessful = accountService.update(testAccount);

        assertThat(updateSuccessful).isTrue();
    }

    /**
     * Tests {@link AccountService#update(Account)} but for null account
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateForNullAccount() {
        accountService.update(null);
    }

    /**
     * Tests {@link AccountService#update(Account)} but for a nonexistent account
     */
    @Test
    public void testUpdateForNonexistentAccount() {
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        when(accountRepository.existsById(ACCOUNT_ID)).thenReturn(false);

        boolean updateSuccessful = accountService.update(testAccount);

        assertThat(updateSuccessful).isFalse();
    }

    /**
     * Tests {@link AccountService#update(Account)} but for a nonexistent customer
     */
    @Test
    public void testUpdateForNonexistentCustomer() {
        when(accountRepository.existsById(ACCOUNT_ID)).thenReturn(true);
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.empty());

        boolean updateSuccessful = accountService.update(testAccount);

        assertThat(updateSuccessful).isFalse();
    }

    /**
     * Tests {@link AccountService#delete(long)}
     */
    @Test
    public void testDelete() {
        when(accountRepository.existsById(ACCOUNT_ID)).thenReturn(true);

        boolean updateSuccessful = accountService.delete(ACCOUNT_ID);

        assertThat(updateSuccessful).isTrue();
    }

    /**
     * Tests {@link AccountService#delete(long)} but for negative ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteForNegativeId() {
        accountService.delete(-1);
    }

    /**
     * Tests {@link AccountService#delete(long)} but for a nonexistent account
     */
    @Test
    public void testDeleteForNonexistentAccount() {
        when(accountRepository.existsById(ACCOUNT_ID)).thenReturn(false);

        boolean updateSuccessful = accountService.delete(ACCOUNT_ID);

        assertThat(updateSuccessful).isFalse();
    }

    /**
     * Tests {@link AccountService#getAccountsForCustomer(long)}
     */
    @Test
    public void testGetAccountsForCustomer() {
        when(accountRepository.findByCustomer(testCustomer)).thenReturn(ImmutableList.of(testAccount));

        List<Account> accountsFoCustomer = accountService.getAccountsForCustomer(CUSTOMER_ID);

        assertThat(accountsFoCustomer).isNotEmpty();
        assertThat(accountsFoCustomer).hasSize(COUNT_OF_ACCOUNTS);
        assertThat(accountsFoCustomer.get(0)).isEqualTo(testAccount);
    }

    /**
     * Tests {@link AccountService#getAccountsForCustomer(long)} but with negative customer ID
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAccountsForCustomerWithNegativeCustomerId() {
        accountService.getAccountsForCustomer(-1);
    }

    /**
     * Tests {@link AccountService#getAccountsForCustomer(long)} but for a customer with no accounts
     */
    @Test
    public void testDeleteForCustomerWithNoAccounts() {
        when(accountRepository.findByCustomer(testCustomer)).thenReturn(ImmutableList.of());

        List<Account> accountsFoCustomer = accountService.getAccountsForCustomer(CUSTOMER_ID);

        assertThat(accountsFoCustomer).isEmpty();
    }
}