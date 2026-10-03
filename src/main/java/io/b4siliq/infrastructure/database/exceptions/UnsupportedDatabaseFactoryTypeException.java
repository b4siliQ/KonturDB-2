package io.b4siliq.infrastructure.database.exceptions;

public class UnsupportedDatabaseFactoryTypeException extends Exception {
    public UnsupportedDatabaseFactoryTypeException() {
        super();
    }

    public UnsupportedDatabaseFactoryTypeException(String message) {
        super(message);
    }

    public UnsupportedDatabaseFactoryTypeException(Throwable err) {
        super(err);
    }

    public UnsupportedDatabaseFactoryTypeException(String message, Throwable err) {
        super(message, err);
    }
}
