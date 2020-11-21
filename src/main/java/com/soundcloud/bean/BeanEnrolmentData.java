package com.soundcloud.bean;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Class for bean representation
 */
class BeanEnrolmentData {

    private String name;
    private Class<?> beanClass;
    private Set<Class<?>> interfaces;
    private Set<Annotation> annotations;
    private Interceptor interceptor;
    private Supplier<?> factory;
    private Object concreteBean;


    protected BeanEnrolmentData() {
    }


    public boolean isInterceptor(){
        return this.interceptor != null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanEnrolmentData that = (BeanEnrolmentData) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(beanClass, that.beanClass) &&
                Objects.equals(interfaces, that.interfaces) &&
                Objects.equals(annotations, that.annotations) &&
                Objects.equals(interceptor, that.interceptor) &&
                Objects.equals(factory, that.factory) &&
                Objects.equals(concreteBean, that.concreteBean);}

    @Override
    public int hashCode() {
        return Objects.hash(name, beanClass, interfaces, annotations, interceptor, factory, concreteBean);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Set<Class<?>> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Set<Class<?>> interfaces) {
        this.interfaces = interfaces;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Supplier<?> getFactory() {
        return factory;
    }

    public void setFactory(Supplier<?> factory) {
        this.factory = factory;
    }

    public Object getConcreteBean() {
        return concreteBean;
    }

    public void setConcreteBean(Object concreteBean) {
        this.concreteBean = concreteBean;
    }
}
