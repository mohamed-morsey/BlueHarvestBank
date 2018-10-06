package io.blueharvest.bank.constant;

/**
 * Container for column, field and parameter names
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
public final class Fields {

    // region parameter names
    public static final String ID_PARAMETER = "id";
    // endregion

    // region generic field names
    public static final String ID_FIELD = "id";
    // endregion

    // region field names of customer
    public static final String NAME_FIELD = "name";
    public static final String SURNAME_FIELD = "surname";
    public static final String ADDRESS_FIELD = "address";
    public static final String POSTCODE_FIELD = "postcode";
    // endregion

    // region generic field account
    public static final String CREDIT_FIELD = "credit";
    // endregion

    private Fields() {
        // Private constructor to prevent instantiation
    }
}
