package io.blueharvest.bank.service;

import com.google.common.collect.ImmutableList;
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

import java.util.List;
import java.util.Optional;

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
    //region field values
    private static final Long CUSTOMER_ID = 1L;
    private static final Long ID = 10L;
    private static final Double CREDIT = 1000.50;
    private static final Long TRANSACTION_ID = 20L;

    private static final Double MODIFIED_CREDIT = 1500.10; // Used for updating account details

    private static final int COUNT_OF_ACCOUNTS = 1;
    //endregion

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
        testCustomer = new Customer(CUSTOMER_ID);
        testAccount = new Account(ID, CREDIT, testCustomer);
        testTransaction = new Transaction(TRANSACTION_ID, CREDIT, testAccount);
    }

    /**
     * Tests {@link AccountService#get(Long)}
     */
    @Test
    public void testGet() {
        when(accountRepository.findById(ID)).thenReturn(testAccount);

        Optional<Account> desiredAccountOptional = accountService.get(ID);

        assertThat(desiredAccountOptional).isPresent();
        assertThat(desiredAccountOptional).hasValueSatisfying(
                account -> {
                    assertThat(account.getId()).isEqualTo(ID);
                    assertThat(account.getCredit()).isEqualTo(CREDIT);
                });
    }

    /**
     * Tests {@link AccountService#get(Long)} but for NULL ID
     */
    @Test(expected = NullPointerException.class)
    public void testGetForNullId() {
        accountService.get(null);
    }

    /**
     * Tests {@link AccountService#get(Long)} but for a nonexistent one
     */
    @Test
    public void testGetForNonexistentAccount() {
        when(accountRepository.findById(ID)).thenReturn(null);

        Optional<Account> desiredAccountOptional = accountService.get(ID);

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
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        when(customerService.get(CUSTOMER_ID)).thenReturn(Optional.of(testCustomer));
        when(transactionService.create(any(Transaction.class))).thenReturn(Optional.of(testTransaction));

        Optional<Account> createdAccountOptional = accountService.create(testAccount);

        assertThat(createdAccountOptional).isPresent();
        assertThat(createdAccountOptional).hasValueSatisfying(
                account -> {
                    assertThat(account.getId()).isEqualTo(ID);
                    assertThat(account.getCredit()).isEqualTo(CREDIT);
                });
    }

    /**
     * Tests {@link AccountService#create(Account)} but for null customer
     */
    @Test(expected = NullPointerException.class)
    public void testCreateForNullCustomer() {
        accountService.create(null);
    }

    /**
     * Tests {@link AccountService#update(Account)}
     */
    @Test
    public void testUpdate() {
        testAccount.setCredit(MODIFIED_CREDIT);
        when(accountRepository.existsById(ID)).thenReturn(true);
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
     * Tests {@link AccountService#update(Account)} for a nonexistent account
     */
    @Test
    public void testUpdateForNonexistentAccount() {
        when(accountRepository.save(testAccount)).thenReturn(testAccount);
        when(accountRepository.existsById(ID)).thenReturn(false);

        boolean updateSuccessful = accountService.update(testAccount);

        assertThat(updateSuccessful).isFalse();
    }

    @Test
    public void delete() {
    }

    @Test
    public void getAccountsForCustomer() {
    }
}