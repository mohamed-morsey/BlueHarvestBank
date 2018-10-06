/*
 *
 * Copyright (c) 2017 Semmtech B.V. All rights reserved.
 *
 *     ___  _____ __  __ __  __ _____ _____ ___ _   _
 *    / __|| ____|  \/  |  \/  |_   _| ____/ __| | | |
 *    \__ \|  _| | |\/| | |\/| | | | |  _|| |  | |_| |
 *     __) | |___| |  | | |  | | | | | |__| |__|  _  |
 *    |___/|_____|_|  |_|_|  |_| |_| |_____\___|_| |_| B.V.
 *
 *
 * Permission to use this software (code), model and/or any associated
 * documentation is granted under the terms of the Semmtech License
 * Agreement. If the license is in any way unknown or unclear please
 * contact Semmtech: info@semmtech.com.
 *
 * THIS SOFTWARE (CODE), MODEL AND/OR DOCUMENTATION IS PROVIDED "AS IS,"
 * AND SEMMTECH MAKES NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY
 * OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT THE USE OF THE SOFTWARE
 * (CODE), MODEL OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY PATENTS,
 * COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS. SEMMTECH WILL NOT BE LIABLE FOR
 * ANY DIRECT, INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF
 * ANY USE OF THE SOFTWARE (CODE), MODEL OR DOCUMENTATION.
 *
 * The name and (registered) trademarks of Semmtech may NOT be used in
 * advertising or publicity pertaining to the software (code), model and/or
 * documentation without specific, written prior permission. Title to
 * rights in this software (code), model and/or any associated documentation
 * will at all times remain with Semmtech.
 *
 */
package io.blueharvest.bank.validation;

import io.blueharvest.bank.model.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static io.blueharvest.bank.constant.Fields.ADDRESS_FIELD;
import static io.blueharvest.bank.constant.Fields.NAME_FIELD;
import static io.blueharvest.bank.constant.Fields.POSTCODE_FIELD;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_ADDRESS_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_NAME_ERROR;
import static io.blueharvest.bank.constant.Messages.BLANK_INVALID_POSTCODE_ERROR;

/**
 * Validator for {@link io.blueharvest.bank.model.Customer}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 */
@Component
public class CustomerValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        final Customer customer = (Customer) obj;

        if (StringUtils.isBlank(customer.getName())) {
            errors.rejectValue(NAME_FIELD, BLANK_INVALID_NAME_ERROR);
        }

        if (StringUtils.isBlank(customer.getAddress())) {
            errors.rejectValue(ADDRESS_FIELD, BLANK_INVALID_ADDRESS_ERROR);
        }

        if (StringUtils.isBlank(customer.getPostcode())) {
            errors.rejectValue(POSTCODE_FIELD, BLANK_INVALID_POSTCODE_ERROR);
        }
    }

}
