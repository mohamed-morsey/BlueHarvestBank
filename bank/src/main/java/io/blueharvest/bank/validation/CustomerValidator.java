package io.blueharvest.bank.validation;

import io.blueharvest.bank.dto.CustomerDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static io.blueharvest.bank.constant.Fields.ADDRESS_FIELD;
import static io.blueharvest.bank.constant.Fields.NAME_FIELD;
import static io.blueharvest.bank.constant.Fields.POSTCODE_FIELD;
import static io.blueharvest.bank.constant.Fields.SURNAME_FIELD;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ADDRESS_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_NAME_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_POSTCODE_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_SURNAME_ERROR;

/**
 * Validator for {@link CustomerDto}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 */
@Component
public class CustomerValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return CustomerDto.class.equals(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final CustomerDto customer = (CustomerDto) obj;

        if (StringUtils.isBlank(customer.getName())) {
            errors.rejectValue(NAME_FIELD, BLANK_INVALID_NAME_ERROR);
        }

        if (StringUtils.isBlank(customer.getSurname())) {
            errors.rejectValue(SURNAME_FIELD, BLANK_INVALID_SURNAME_ERROR);
        }

        if (StringUtils.isBlank(customer.getAddress())) {
            errors.rejectValue(ADDRESS_FIELD, BLANK_INVALID_ADDRESS_ERROR);
        }

        if (StringUtils.isBlank(customer.getPostcode())) {
            errors.rejectValue(POSTCODE_FIELD, BLANK_INVALID_POSTCODE_ERROR);
        }
    }

}
