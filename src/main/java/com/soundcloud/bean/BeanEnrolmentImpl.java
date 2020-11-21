package com.soundcloud.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean enrollment class implementation
 */
public class BeanEnrolmentImpl implements BeanEnrolment {
    private final Set<BeanEnrolmentData> beans;
    private final BeanFactory beanFactory;

    public BeanEnrolmentImpl() {
        this.beans = new HashSet<>();
        this.beanFactory = new BeanFactory();
    }


    @Override
    public <T> void enrollBean(T bean) {
        BeanEnrolmentData data = setBeanData(bean.getClass());
        data.setConcreteBean(bean);
        addBean(data);

    }

    /**
     * method add bean to bean beans map
     *
     * @param data bean enrollment data
     */
    private void addBean(BeanEnrolmentData data) {
        beans.stream()
                .filter(enrolmentData -> enrolmentData.getName().equals(data.getName()))
                .findFirst()
                .ifPresent(enrolmentData -> {
                    throw new NotUniqueBeanException("Bean with name " + enrolmentData.getName() + " already enrolled");
                });
        beans.add(data);
    }

    /**
     * method set bean data
     *
     * @param beanClass bean class
     * @return bean enrollment data
     */
    private <T> BeanEnrolmentData setBeanData(Class<T> beanClass) {
        Bean beanAnnotation = beanClass.getAnnotation(Bean.class);
        if (beanAnnotation == null) {
            throw new AnnotationAbsenceException("Annotation @Bean is absent");
        }
        BeanEnrolmentData data = new BeanEnrolmentData();
        data.setBeanClass(beanClass);
        data.setInterfaces(Arrays.stream(beanClass.getInterfaces()).collect(Collectors.toSet()));
        data.setAnnotations(Arrays.stream(beanClass.getAnnotations()).collect(Collectors.toSet()));
        Interceptor interceptor = beanClass.getAnnotation(Interceptor.class);
        if (interceptor != null) {
            data.setInterceptor(interceptor);
        }
        String name = beanAnnotation.beanName();
        data.setName("".equals(name) ? beanClass.getSimpleName() : name.trim());
        return data;
    }

    @Override
    public <T> void enrollBean(Class<T> beanClass) {
        BeanEnrolmentData data = setBeanData(beanClass);
        data.setFactory(createFactory(data));
        addBean(data);
    }

    /**
     * method create supplier for bean creation by bean enrollment data
     *
     * @param data bean enrollment data
     * @return supplier for creation beans
     */
    private Supplier<Object> createFactory(BeanEnrolmentData data) {
        Class<?> beanClass = data.getBeanClass();
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new BeanInitialisationException("Must be only 1 constructor for bean class " + beanClass.getSimpleName());
        }
        return () -> {
            Constructor<?> constructor = constructors[0];
            if (constructor.getParameterCount() > 0) {
                Parameter[] parameters = constructor.getParameters();
                Object[] args = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Class<?> type = parameters[i].getType();
                    BeanQualifier beanQualifier = parameters[i].getAnnotation(BeanQualifier.class);
                    if (beanQualifier != null) {
                        Predicate<BeanEnrolmentData> searchBean = searchInfo -> searchInfo.getName().equals(beanQualifier.value());
                        args[i] = getBean(searchBean);
                    } else {
                        args[i] = getBean(type);
                    }
                }
                try {
                    return constructor.newInstance(args);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new BeanInitialisationException("Failed to instantiate bean", e);
                }
            } else {
                try {
                    return beanClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new BeanInitialisationException("Failed to instantiate bean", e);
                }
            }
        };
    }

    /**
     * method get bean by bean factory
     *
     * @param searchBean search bean factory
     * @return bean
     */
    @SuppressWarnings("unchecked")
    private <T> T getBean(Predicate<BeanEnrolmentData> searchBean) {
        List<BeanEnrolmentData> registryInfoList = beans
                .stream()
                .filter(searchBean)
                .collect(Collectors.toList());

        if (registryInfoList.size() > 1) {
            String multipleNames = registryInfoList.stream().map(BeanEnrolmentData::getName).collect(Collectors.joining(", "));
            throw new NotUniqueBeanException("Multiple implementations found: " + multipleNames);
        } else {
            return (T) registryInfoList.stream().map(this::mapToBean).findFirst().orElse(null);
        }
    }

    /**
     * method return bean by bean enrollment data
     *
     * @param registryInfo registry info
     * @return bean
     */
    @SuppressWarnings("unchecked")
    private <T> T mapToBean(BeanEnrolmentData registryInfo) {
        T service = (T) beanFactory.getBean(registryInfo);
        Set<BeanEnrolmentData> availableInterceptors = hasAnnotation(Interceptor.class, registryInfo, BeanEnrolmentData::isInterceptor);
        if (!availableInterceptors.isEmpty()) {
            List<BeanInterceptor> interceptors = getList(availableInterceptors);
            return getServiceProxy(service, registryInfo, interceptors);
        }
        return service;
    }

    /**
     * method return list from bean enrollment data set
     * @param data set of bean enrollment data
     * @param <T> bean type
     * @return list from bean enrollment data set
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> getList(Set<BeanEnrolmentData> data) {
        return data.stream()
                .map(info -> (T) beanFactory.getBean(info))
                .collect(Collectors.toList());
    }

    /**
     * method check if class has annotation
     * @param annotation annotation that class should have
     * @param info bean info
     * @param predicate predicate
     * @return set of bean enrollment data
     */
    private Set<BeanEnrolmentData> hasAnnotation(Class<? extends Annotation> annotation, BeanEnrolmentData info, Predicate<? super BeanEnrolmentData> predicate) {
        return beans.stream()
                .filter(predicate)
                .filter(interceptorInfo -> info.getAnnotations()
                        .stream()
                        .anyMatch(a -> a.annotationType().equals(annotation)))
                .collect(Collectors.toSet());

    }

    /**
     * method return proxy service for bean
     * @param service service for proxy
     * @param info bean enrollment data
     * @param interceptors type interceptors
     * @param <T> bean type
     * @return proxy service
     */
    @SuppressWarnings("unchecked")
    private <T> T getServiceProxy(T service, BeanEnrolmentData info, List<BeanInterceptor> interceptors) {
        Class<?>[] toProxy = new Class[info.getInterfaces().size()];
        Class<?>[] interfaces = info.getInterfaces().toArray(toProxy);
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces,
                (proxy, method, args) -> {

                    try {
                        for (BeanInterceptor interceptor : interceptors) {
                            interceptor.before(proxy, service, method, args);
                        }
                        Object invoked = method.invoke(service, args);
                        for (BeanInterceptor interceptor : interceptors) {
                            interceptor.success(proxy, service, method, args);
                        }
                        return invoked;
                    } catch (Exception e) {
                        for (BeanInterceptor interceptor : interceptors) {
                            interceptor.fail(proxy, service, method, args);
                        }
                        throw new IllegalStateException("Exception during proxy invocation", e);
                    }
                });
    }


    @Override
    public <T> T getBean(Class<T> beanClass) {
        Bean bean = beanClass.getAnnotation(Bean.class);
        String beanName = bean != null && bean.beanName().trim().length() > 0 ? bean.beanName().trim() : null;
        Predicate<BeanEnrolmentData> searchBean = data -> data.getName().equals(beanName) || data.getBeanClass().equals(beanClass) || data.getInterfaces().contains(beanClass);
        return getBean(searchBean);
    }

    @Override
    public <T> T getBean(String beanName) {
        Predicate<BeanEnrolmentData> searchBean = data -> data.getName().equals(beanName) || data.getBeanClass().getSimpleName().equals(beanName);
        return getBean(searchBean);
    }

    @Override
    public <T> boolean removeBean(T bean) {
        BeanEnrolmentData enrolmentData = setBeanData(bean.getClass());
        enrolmentData.setConcreteBean(bean);
        return beans.remove(enrolmentData);
    }

    @Override
    public void destroy() {
        beans.clear();
        beanFactory.destroy();
    }

    /**
     * class bean factory
     */
    private static class BeanFactory {

        private final Map<BeanEnrolmentData, Object> beans = new ConcurrentHashMap<>();

        Object getBean(BeanEnrolmentData info) {

            if (info.getConcreteBean() != null) {
                beans.put(info, info.getConcreteBean());
            } else if (!beans.containsKey(info)) {
                final Object bean = info.getFactory().get();
                beans.put(info, bean);
            }
            return beans.get(info);
        }

        void destroy() {
            beans.clear();
        }
    }

}

