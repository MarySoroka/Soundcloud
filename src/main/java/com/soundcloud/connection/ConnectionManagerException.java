package com.soundcloud.connection;

/**
 * Connection manager exception
 */
public class ConnectionManagerException  extends Exception{
    public ConnectionManagerException() {
    }

    public ConnectionManagerException(String message) {
        super(message);
    }

    public ConnectionManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionManagerException(Throwable cause) {
        super(cause);
    }

    public ConnectionManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
