package com.soundcloud.bean;

/**
 * Bean initialisation exception
 */
public class BeanInitialisationException extends BeanEnrolmentException {

    public BeanInitialisationException(String s) {
        super(s);
    }

    public BeanInitialisationException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
