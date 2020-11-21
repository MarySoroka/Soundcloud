package com.soundcloud.subscription;

import com.soundcloud.service.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * Subscription service interface, that contains method for business logic realisation
 */
public interface SubscriptionService {
    /**
     * method return subscription by user id
     *
     * @param userId user id
     * @return if subscription presents, then return optional of subscription, else optional.empty()
     * @throws ServiceException if we get exception from dao
     */
    Optional<Subscription> getSubscriptionByUserId(Long userId) throws ServiceException;

    /**
     * method return all subscriptions
     *
     * @return list of subscriptions
     * @throws ServiceException if we get exception from dao
     */
    List<Subscription> getAllSubscriptions() throws ServiceException;

    /**
     * method return subscription by id
     *
     * @param subscriptionId subscription id
     * @return if subscription presents, then return optional of subscription, else optional.empty()
     * @throws ServiceException if we get exception from dao
     */
    Optional<Subscription> getSubscriptionId(Long subscriptionId) throws ServiceException;

    /**
     * method save subscription
     *
     * @param subscription subscription
     * @return subscription id
     * @throws ServiceException if we get exception from dao
     */
    Long saveSubscription(Subscription subscription) throws ServiceException;

    /**
     * method delete subscription
     *
     * @param subscriptionId subscription id
     * @return if subscription has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteSubscription(Long subscriptionId) throws ServiceException;

    /**
     * method update subscription
     *
     * @param subscription subscription
     * @return if subscription has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean updateSubscription(Subscription subscription) throws ServiceException;
}
