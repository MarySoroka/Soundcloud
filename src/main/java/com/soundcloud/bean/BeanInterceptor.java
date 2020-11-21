package com.soundcloud.bean;

import java.lang.reflect.Method;

/**
 * Bean interceptor interface
 */
public interface BeanInterceptor {
    /**
     * method for executing something before method in which interceptor in used
     *
     * @param proxy   proxy object for defining actions
     * @param service service
     * @param method  method in which interceptor in used
     * @param args    args
     */
    void before(Object proxy, Object service, Method method, Object[] args);

    /**
     * method for executing something if method successes in which interceptor in used
     *
     * @param proxy   proxy object for defining actions
     * @param service service
     * @param method  method in which interceptor in used
     * @param args    args
     */
    void success(Object proxy, Object service, Method method, Object[] args);

    /**
     * method for executing something if method failed in which interceptor in used
     *
     * @param proxy   proxy object for defining actions
     * @param service service
     * @param method  method in which interceptor in used
     * @param args    args
     */
    void fail(Object proxy, Object service, Method method, Object[] args);
}
