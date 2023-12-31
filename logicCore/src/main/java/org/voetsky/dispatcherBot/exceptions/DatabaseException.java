package org.voetsky.dispatcherBot.exceptions;

import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;

public class DatabaseException extends LogicCoreException {
    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

}
