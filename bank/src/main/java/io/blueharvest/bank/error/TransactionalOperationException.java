package io.blueharvest.bank.error;

/**
 * Thrown when an error occurs during a transactional operation
 *
 * @author Mohamed Morsey
 * Date: 2018-10-08
 **/
public class TransactionalOperationException extends RuntimeException {
    public TransactionalOperationException(String message) {
        super(message);
    }

    public TransactionalOperationException(String message, Throwable exp) {
        super(message, exp);
    }
}
