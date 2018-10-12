package io.blueharvest.bank.constant;

/**
 * Container for field values to be used in testing
 *
 * @author Mohamed Morsey
 * Date: 2018-10-11
 **/
public final class FieldValues {

    //region field values for customers
    public static final long CUSTOMER_ID = 1L;
    public static final String NAME = "John";
    public static final String SURNAME = "Smith";
    public static final String ADDRESS = "Amsterdam";
    public static final String POSTCODE = "1234AB";
    public static final String MODIFIED_POSTCODE = "6789XY"; // Used for updating customer details

    public static final int COUNT_OF_CUSTOMERS = 1;
    //endregion

    //region field values for accounts
    public static final long ACCOUNT_ID = 1L;
    public static final Double CREDIT = 1000.50;
    public static final Double MODIFIED_CREDIT = 1500.10; // Used for updating account details

    public static final int COUNT_OF_ACCOUNTS = 1;
    //endregion

    //region field values for transactions
    public static final long TRANSACTION_ID = 1L;
    public static final Double AMOUNT = 1000.50D;

    public static final int COUNT_OF_TRANSACTIONS = 1;
    //endregion

    public FieldValues() {
        // Private constructor to prevent instantiation
    }
}
