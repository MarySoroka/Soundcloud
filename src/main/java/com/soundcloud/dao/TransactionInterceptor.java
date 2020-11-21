package com.soundcloud.dao;

import com.soundcloud.bean.Bean;
import com.soundcloud.bean.BeanInterceptor;
import com.soundcloud.bean.Interceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Class, that implements BeanInterceptor
 */
@Bean
@Interceptor(clazz = TransactionSupport.class)
public class TransactionInterceptor implements BeanInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(TransactionInterceptor.class);
    private final TransactionManager transactionManager;

    public TransactionInterceptor(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void before(Object proxy, Object service, Method method, Object[] args) {
        if (isTransactional(method)) {
            try {
                this.transactionManager.beginTransaction();
            } catch (TransactionException e) {
                LOGGER.error("Failed the beginning of the transaction: {}", e.getMessage());
                throw new IllegalStateException("Failed the beginning of the transaction", e);
            }
        }
    }

    @Override
    public void success(Object proxy, Object service, Method method, Object[] args) {
        if (isTransactional(method)) {
            try {
                this.transactionManager.commitTransaction();
            } catch (TransactionException e) {
                LOGGER.error("Failed the commit of the transaction: {}", e.getMessage());
                throw new IllegalStateException("Failed the commit of the transaction", e);
            }
        }
    }

    @Override
    public void fail(Object proxy, Object service, Method method, Object[] args) {
        if (isTransactional(method)) {
            try {
                this.transactionManager.rollbackTransaction();
            } catch (TransactionException e) {
                LOGGER.error("Failed the rollback of the transaction: {}", e.getMessage());
                throw new IllegalStateException("Failed the rollback of the transaction", e);
            }
        }
    }

    /**
     * method check if annotation of the method is transactional
     *
     * @param method method that is checked
     * @return if annotation is absent - false, else true
     */
    private boolean isTransactional(Method method) {
        return method.getAnnotation(Transactional.class) != null;
    }
}
