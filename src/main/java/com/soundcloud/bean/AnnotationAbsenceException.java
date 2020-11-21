package com.soundcloud.bean;

/**
 * Exception for annotation absence
 */
public class AnnotationAbsenceException extends BeanEnrolmentException{

    public AnnotationAbsenceException(String s) {
        super(s);
    }

    public AnnotationAbsenceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationAbsenceException(Throwable throwable) {
        super(throwable);
    }

    public AnnotationAbsenceException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
