package com.soundcloud.dao;

/**
 * Exception in  dao layer
 */
public class DaoException extends Exception {

    public DaoException() {
    }

    public DaoException(String s) {
        super(s);
    }

    public DaoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DaoException(Throwable throwable) {
        super(throwable);
    }

    public DaoException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
