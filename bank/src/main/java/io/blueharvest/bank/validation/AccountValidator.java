package io.blueharvest.bank.validation;

import io.blueharvest.bank.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static io.blueharvest.bank.constant.Fields.CREDIT_FIELD;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_INITIAL_CREDIT_ERROR;

/**
 * Validator for {@link Account}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 */
@Component
public class AccountValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final Account account = (Account) obj;

        if ((account.getCredit() == null) || (account.getCredit() <= 0)) {
            errors.rejectValue(CREDIT_FIELD, BLANK_INVALID_INITIAL_CREDIT_ERROR);
        }

    }

}
