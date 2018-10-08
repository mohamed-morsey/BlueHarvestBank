package io.blueharvest.bank.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.blueharvest.bank.constant.Messages.INVALID_PARAMETER_ERROR;
import static io.blueharvest.bank.constant.Messages.OBJECT_NOT_FOUND_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Handler for exceptions thrown by REST controller
 *
 * @author Mohamed Morsey
 * Date: 2018-04-23
 */
@RestControllerAdvice
public class BankExceptionHandler {
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Handler for {@link IllegalArgumentException} that can be thrown in case of invalid parameter is passed
     *
     * @param exp
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleIllegalArgumentException(IllegalArgumentException exp, HttpServletResponse response) throws IOException {
        String errorMessage = INVALID_PARAMETER_ERROR + ": " + exp.getMessage();

        logger.error(errorMessage, exp);
        response.sendError(SC_BAD_REQUEST, errorMessage);
    }

    /**
     * Handler for {@link ObjectNotFoundException} that can be thrown if an item does not exist
     *
     * @param exp
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    private void handleResourceNotFoundException(ObjectNotFoundException exp, HttpServletResponse response) throws IOException {
        String errorMessage = OBJECT_NOT_FOUND_ERROR + ": " + exp.getMessage();

        logger.error(errorMessage, exp);
        response.sendError(SC_NOT_FOUND, errorMessage);
    }
}