package io.blueharvest.bank.rest;

import com.google.common.collect.ImmutableList;
import io.blueharvest.bank.error.BankExceptionHandler;
import io.blueharvest.bank.model.Account;
import io.blueharvest.bank.model.Transaction;
import io.blueharvest.bank.service.AccountService;
import io.blueharvest.bank.service.TransactionService;
import io.blueharvest.bank.utils.StandaloneMvcTestViewResolver;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static io.blueharvest.bank.constant.FieldValues.ACCOUNT_ID;
import static io.blueharvest.bank.constant.FieldValues.AMOUNT;
import static io.blueharvest.bank.constant.FieldValues.CREDIT;
import static io.blueharvest.bank.constant.FieldValues.TRANSACTION_ID;
import static io.blueharvest.bank.constant.Fields.ACCOUNT_ID_PARAMETER;
import static io.blueharvest.bank.constant.Paths.TRANSACTIONS_CONTEXT_PTAH;
import static io.blueharvest.bank.rest.TransactionController.TRANSACTIONS_ATTRIBUTE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link TransactionController}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-11
 **/
@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private static final String INVALID_ACCOUNT_ID = "NonNumeric";

    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private Logger logger;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;
    private Account testAccount;
    private Transaction testTransaction;

    @Before
    public void setUp() throws Exception {
        testAccount = new Account(ACCOUNT_ID, CREDIT);
        testTransaction = new Transaction(TRANSACTION_ID, AMOUNT, testAccount);

        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setViewResolvers(new StandaloneMvcTestViewResolver())
                .setControllerAdvice(new BankExceptionHandler()).build();
    }

    /**
     * Tests {@link TransactionController#getTransactionForAccount(String, Model)}
     *
     * @throws Exception
     */
    @Test
    public void testGetTransactionForAccount() throws Exception {
        List<Transaction> transactions = ImmutableList.of(testTransaction);
        when(accountService.get(ACCOUNT_ID)).thenReturn(Optional.of(testAccount));
        when(transactionService.getTransactionsForAccount(ACCOUNT_ID)).thenReturn(transactions);

        this.mockMvc.perform(get("/" + TRANSACTIONS_CONTEXT_PTAH)
                .param(ACCOUNT_ID_PARAMETER, String.valueOf(ACCOUNT_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + TRANSACTIONS_CONTEXT_PTAH))
                .andExpect(model().attribute(TRANSACTIONS_ATTRIBUTE_NAME, equalTo(ImmutableList.of(testTransaction))));
    }

    /**
     * Tests {@link TransactionController#getTransactionForAccount(String, Model)} but with an invalid account ID
     *
     * @throws Exception
     */
    @Test
    public void testGetTransactionForAccountWithInvalidAccountId() throws Exception {

        this.mockMvc.perform(get("/" + TRANSACTIONS_CONTEXT_PTAH)
                .param(ACCOUNT_ID_PARAMETER, String.valueOf(INVALID_ACCOUNT_ID)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests {@link TransactionController#getTransactionForAccount(String, Model)} but for a nonexistent account
     *
     * @throws Exception
     */
    @Test
    public void testGetTransactionForAccountForNonexistentAccount() throws Exception {
        when(accountService.get(ACCOUNT_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/" + TRANSACTIONS_CONTEXT_PTAH)
                .param(ACCOUNT_ID_PARAMETER, String.valueOf(ACCOUNT_ID)))
                .andExpect(status().isNotFound());
    }
}