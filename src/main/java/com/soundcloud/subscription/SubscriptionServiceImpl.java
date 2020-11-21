package com.soundcloud.subscription;

import com.soundcloud.bean.Bean;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.TransactionSupport;
import com.soundcloud.dao.Transactional;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.SERVICE_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.SERVICE_FATAL_EXCEPTION;
/**
 * Class, that implements SubscriptionService and execute business login that relate to album
 */
@Bean(beanName = "subscriptionService")
@TransactionSupport
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionDao subscriptionDao;
    private static final Logger LOGGER = LogManager.getLogger(SubscriptionServiceImpl.class);

    public SubscriptionServiceImpl(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    @Override
    public Optional<Subscription> getSubscriptionByUserId(Long userId) throws ServiceException {
        try {
            return subscriptionDao.getSubscriptionByUserId(userId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }

    }

    @Override
    public List<Subscription> getAllSubscriptions() throws ServiceException {
        try {
            return subscriptionDao.getAll();
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public Optional<Subscription> getSubscriptionId(Long subscriptionId) throws ServiceException {
        try {
            return subscriptionDao.getById(subscriptionId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    @Transactional
    public Long saveSubscription(Subscription subscription) throws ServiceException {
        try {
            LocalDate nextPayment = subscription.getSubscriptionDate().plusMonths(1L);
            subscription.setSubscriptionDate(nextPayment);
            return subscriptionDao.save(subscription);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteSubscription(Long subscriptionId) throws ServiceException {
        try {
            return subscriptionDao.delete(subscriptionId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean updateSubscription(Subscription subscription) throws ServiceException {
        try {
            return subscriptionDao.update(subscription.getSubscriptionId(),subscription);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
