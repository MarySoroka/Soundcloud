package com.soundcloud.bean;

/**
 * Bean enrollment exception
 */
public class BeanEnrolmentException  extends RuntimeException{
    public BeanEnrolmentException() {
    }

    public BeanEnrolmentException(String s) {
        super(s);
    }

    public BeanEnrolmentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BeanEnrolmentException(Throwable throwable) {
        super(throwable);
    }

    public BeanEnrolmentException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
