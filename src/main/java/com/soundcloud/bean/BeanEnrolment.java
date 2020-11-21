package com.soundcloud.bean;

/**
 * Bean enrollment interface
 */
public interface BeanEnrolment {
    /**
     * method enroll bean
     *
     * @param bean bean
     */
    <T> void enrollBean(T bean);

    /**
     * method enroll bean by bean class
     *
     * @param beanClass beanClass
     */
    <T> void enrollBean(Class<T> beanClass);

    /**
     * get bean by bean class
     *
     * @param beanClass bean class
     * @return bean
     */
    <T> T getBean(Class<T> beanClass);

    /**
     * get bean by bean name
     *
     * @param beanName bean name
     * @return bean
     */
    <T> T getBean(String beanName);

    /**
     * method remove bean
     *
     * @param bean bean
     * @return if bean has been deleted, then return true, else false
     */
    <T> boolean removeBean(T bean);

    /**
     * method destroy application context
     */
    void destroy();
}
