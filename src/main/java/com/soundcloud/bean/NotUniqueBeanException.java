package com.soundcloud.bean;

/**
 * Exception if no unique bean
 */
public class NotUniqueBeanException extends BeanEnrolmentException {

    public NotUniqueBeanException(String s) {
        super(s);
    }


}
