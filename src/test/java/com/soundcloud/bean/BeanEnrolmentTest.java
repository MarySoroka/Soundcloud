package com.soundcloud.bean;

import com.soundcloud.application.ApplicationContext;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.role.Role;
import com.soundcloud.role.RoleBuilder;
import com.soundcloud.role.RoleDaoImpl;
import com.soundcloud.role.RoleServiceImpl;
import com.soundcloud.user.UserService;
import com.soundcloud.user.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;

@RunWith(JUnit4.class)
public class BeanEnrolmentTest {
    private static final Logger LOGGER = LogManager.getLogger(BeanEnrolmentTest.class);
    private UserService serviceImpl;

    @Before
    public void init() {
        ApplicationContext.initialize();
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(transactionManager);

        RoleServiceImpl roleService = new RoleServiceImpl(new RoleDaoImpl(new RoleBuilder(), connectionManager));
        serviceImpl = ApplicationContext.getInstance().getBean(UserServiceImpl.class);

    }

    @Test
    public void shouldRegisterAndReturnServiceByInterface() {
        BeanEnrolment provider = new BeanEnrolmentImpl();
        provider.enrollBean(serviceImpl);
        UserService serviceFound = provider.getBean(UserService.class);
        Assert.assertEquals(serviceImpl, serviceFound);
    }

    @Test
    public void shouldRegisterAndReturnServiceByName() {
        BeanEnrolment provider = new BeanEnrolmentImpl();
        provider.enrollBean(serviceImpl);
        UserService serviceFound = provider.getBean("UserService");
        Assert.assertEquals(serviceImpl.getClass(), serviceFound.getClass());
    }

    @Test
    public void shouldRemoveBean() {
        BeanEnrolment provider = new BeanEnrolmentImpl();
        provider.enrollBean(serviceImpl);
        LOGGER.info(serviceImpl.toString());
        LOGGER.info(provider.getBean("UserService").toString());
        Assert.assertTrue(provider.removeBean(serviceImpl));
    }

    @Test(expected = NotUniqueBeanException.class)
    public void shouldThrowExceptionDueToSameBean() {
        BeanEnrolment provider = new BeanEnrolmentImpl();
        provider.enrollBean(serviceImpl);
        provider.enrollBean(serviceImpl);

    }

    @Test(expected = AnnotationAbsenceException.class)
    public void shouldThrowExceptionDueToMissBeanAnnotation() {
        BeanEnrolment provider = new BeanEnrolmentImpl();
        provider.enrollBean(new Role());

    }

}
