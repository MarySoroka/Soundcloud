package com.soundcloud.subscription;

import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.EntityDao;

import java.util.Optional;

/**
 * Subscription dao interface, that contains method for database interaction
 */
public interface SubscriptionDao extends EntityDao<Subscription, Long> {
    /**
     * method return subscription by user id
     *
     * @param userId user id
     * @return if subscription presents, then return optional of subscription, else optional.empty()
     * @throws DaoException if we get exception in sql request
     */
    Optional<Subscription> getSubscriptionByUserId(Long userId) throws DaoException;
}
