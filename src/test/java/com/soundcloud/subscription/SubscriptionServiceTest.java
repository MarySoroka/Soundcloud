package com.soundcloud.subscription;

import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.service.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;

@RunWith(JUnit4.class)
public class SubscriptionServiceTest {
    public SubscriptionServiceTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        SubscriptionBuilder artistBuilder = new SubscriptionBuilder();

        subscriptionDao = Mockito.spy(new SubscriptionDaoImpl(connectionManager, artistBuilder));
        this.subscriptionService = Mockito.spy(new SubscriptionServiceImpl(subscriptionDao));

        subscription = new Subscription(1L, LocalDate.now(), 1L, SubscriptionStatus.ACTIVE);
    }

    private final Subscription subscription;
    @Mock
    private final SubscriptionDao subscriptionDao;
    @Mock
    private final SubscriptionService subscriptionService;

    @Test
    public void getSubscriptionByUserId_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionDao).getSubscriptionByUserId(Mockito.anyLong());
        Assert.assertTrue(subscriptionService.getSubscriptionByUserId(1L).isPresent());


    }

    @Test
    public void getAllSubscriptions_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>()).when(subscriptionDao).getAll();
        Assert.assertEquals(0, subscriptionService.getAllSubscriptions().size());
    }

    @Test
    public void getSubscriptionId_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionDao).getById(Mockito.anyLong());
        Assert.assertTrue(subscriptionService.getSubscriptionId(1L).isPresent());

    }


    @Test
    public void saveSubscription_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(1L).when(subscriptionDao).save(Mockito.any(Subscription.class));
        Assert.assertEquals(new Long(1), subscriptionService.saveSubscription(subscription));

    }

    @Test
    public void deleteSubscription_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(subscriptionDao).delete(Mockito.anyLong());
        Assert.assertTrue(subscriptionService.deleteSubscription(1L));

    }

    @Test
    public void updateSubscription_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(subscriptionDao).update(Mockito.anyLong(), Mockito.any(Subscription.class));
        Assert.assertTrue(subscriptionService.updateSubscription(subscription));

    }


}