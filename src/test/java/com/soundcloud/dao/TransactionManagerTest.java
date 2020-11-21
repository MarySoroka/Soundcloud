package com.soundcloud.dao;


import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.connection.ConnectionPoolTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RunWith(JUnit4.class)
public class TransactionManagerTest extends Assert {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolTest.class);
    private static final int N_THREADS = 20;
    private static final int THREAD_SLEEP = 1_000;
    private static final int POOL_CAPACITY = 10;

    private DataSource dataSource;
    private TransactionManager transactionManager;

    @Before
    public void init() {
        dataSource = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);
        transactionManager = Mockito.spy(new TransactionManagerImpl(dataSource));
    }

    @Test
    public void shouldGetConnection() throws InterruptedException, SQLException, RuntimeException, TransactionException {
        transactionManager.beginTransaction();
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        IntStream.range(0, N_THREADS).forEach(i -> executorService.submit(() -> {
            LOGGER.info("Try to get connection");
            Connection c = null;
            try {
                c = transactionManager.getConnection();
            } catch (TransactionException e) {
                e.printStackTrace();
            }
            PreparedStatement s = null;
            int j = 0;
            try {
                s = c.prepareStatement(sql);
                s.setString(1, "test_1_name" + j);
                s.setString(2, "test_1_email" + j);
                LOGGER.info("Update query");
                int inserted = s.executeUpdate();
                assertEquals(1, inserted);
            } catch (SQLException e) {
                LOGGER.error("Can't update");
            }

        }));
        transactionManager.commitTransaction();
        executorService.awaitTermination(N_THREADS / POOL_CAPACITY, TimeUnit.SECONDS);
    }


    @After
    public void destroy() throws DataSourceException {
        dataSource.closeConnections();
    }

}
