package com.soundcloud.validation;

public class ValidatorException extends Exception {
    public ValidatorException() {
    }

    public ValidatorException(String s) {
        super(s);
    }

    public ValidatorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ValidatorException(Throwable throwable) {
        super(throwable);
    }

    public ValidatorException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
