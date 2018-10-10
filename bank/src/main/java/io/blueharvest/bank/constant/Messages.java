package io.blueharvest.bank.constant;

/**
 * Container for success and error messages
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
public final class Messages {

    //region success messages
    public static final String COUNT_CLIENTS_READ_SUCCESSFULLY = "%d clients read successfully";
    //endregion

    //region generic error messages
    public static final String INVALID_ID_ERROR = "Invalid ID";
    public static final String INVALID_PARAMETER_ERROR = "Invalid parameter value";
    public static final String OBJECT_NOT_FOUND_ERROR = "Object not found";
    public static final String OPERATION_FAILURE_ERROR = "Operation failed";
    //endregion

    //region error messages for customers
    public static final String BLANK_INVALID_NAME_ERROR = "Blank or invalid name";
    public static final String BLANK_INVALID_SURNAME_ERROR = "Blank or invalid surname";
    public static final String BLANK_INVALID_ADDRESS_ERROR = "Blank or invalid address";
    public static final String BLANK_INVALID_POSTCODE_ERROR = "Blank or invalid postcode";
    public static final String CUSTOMER_NULL_ERROR = "Customer cannot be null";
    public static final String CUSTOMER_NOT_FOUND_ERROR = "Customer not found";
    //endregion

    //region error messages for accounts
    public static final String BLANK_INVALID_INITIAL_CREDIT_ERROR = "Blank or invalid initial credit";
    public static final String ACCOUNT_NULL_ERROR = "Account cannot be null";
    public static final String ACCOUNT_NOT_FOUND_ERROR = "Account not found";
    public static final String ACCOUNT_CREATION_FAILED_ERROR = "Failed to create account";
    //endregion

    //region error messages for transactions
    public static final String TRANSACTION_NULL_ERROR = "Transaction cannot be null";
    public static final String TRANSACTION_CREATION_FAILED_ERROR = "Failed to create transaction";
    //endregion

    private Messages() {
        // Private constructor to prevent instantiation
    }
}
