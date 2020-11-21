package com.soundcloud.bean;


import java.lang.annotation.*;

/**
 * Annotation for interception of method
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor {

    Class<? extends Annotation> clazz();
}
