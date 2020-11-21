package com.soundcloud.connection;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.dao.DataSourceImpl;
import com.soundcloud.dao.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RunWith(JUnit4.class)
public class ConnectionPoolTest {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolTest.class);
    private static final int N_THREADS = 67;
    private static final int POOL_CAPACITY = 20;
    private DataSourceImpl manager;
    private ConnectionPool connectionPool;

    @Before
    public void init() {
        manager = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);
        connectionPool = Mockito.spy(manager.getConnectionPool());
    }

    @Test
    public void shouldGetConnection() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        IntStream.range(0, N_THREADS).forEach(i -> executorService.submit(() -> {
            try (Connection connection = connectionPool.getConnection()) {
                LOGGER.info(i);
                Assert.assertTrue(connection instanceof Proxy);
            } catch (SQLException | IllegalStateException e) {
                LOGGER.error(e);
            }
        }));
        executorService.awaitTermination(N_THREADS / POOL_CAPACITY, TimeUnit.SECONDS);
        Mockito.verify(connectionPool, Mockito.times(N_THREADS)).getConnection();
        Mockito.verify(connectionPool, Mockito.times(N_THREADS)).releaseConnection();

    }

    @After
    public void destroy() throws DataSourceException {
        manager.closeConnections();
    }
}
