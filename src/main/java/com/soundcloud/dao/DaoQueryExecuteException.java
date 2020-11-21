package com.soundcloud.dao;

/**
 * Execution of th query exception in dao layer
 */
public class DaoQueryExecuteException extends DaoException {
    public DaoQueryExecuteException() {
    }

    public DaoQueryExecuteException(String s) {
        super(s);
    }

    public DaoQueryExecuteException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DaoQueryExecuteException(Throwable throwable) {
        super(throwable);
    }

    public DaoQueryExecuteException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
