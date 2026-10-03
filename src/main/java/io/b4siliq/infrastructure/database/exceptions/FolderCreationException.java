package io.b4siliq.infrastructure.database.exceptions;

public class FolderCreationException extends Exception {
    public FolderCreationException() {
        super();
    }

    public FolderCreationException(String message) {
        super(message);
    }

    public FolderCreationException(Throwable err) {
        super(err);
    }

    public FolderCreationException(String message, Throwable err) {
        super(message, err);
    }
}
