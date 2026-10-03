package io.b4siliq.infrastructure.database.exceptions;

public class TableOperationException extends RuntimeException {
    public TableOperationException() {
        super();
    }

    public TableOperationException(String message) {
        super(message);
    }

    public TableOperationException(Throwable err) {
        super(err);
    }

    public TableOperationException(String message, Throwable err) {
        super(message, err);
    }
}
