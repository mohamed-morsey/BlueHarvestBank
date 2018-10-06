package io.blueharvest.bank.constant;

/**
 * Container for success and error messages
 *
 * @author Mohamed Morsey
 * Date: 2018-08-29
 **/
public final class Messages {

    //region success messages
    public static final String COUNT_CLIENTS_READ_SUCCESSFULLY = "%d clients read successfully";
    //endregion

    //region error messages
    public static final String ID_NULL_ERROR = "ID cannot be null";
    public static final String BLANK_INVALID_NAME_ERROR = "Blank or invalid name";
    public static final String BLANK_INVALID_ADDRESS_ERROR = "Blank or invalid address";
    public static final String BLANK_INVALID_POSTCODE_ERROR = "Blank or invalid postcode";
    //endregion

    private Messages() {
        // Private constructor to prevent instantiation
    }
}
