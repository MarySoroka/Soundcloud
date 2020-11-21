package com.soundcloud.dao;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.connection.ConnectionPool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;

@RunWith(JUnit4.class)
public class DataSourceTest extends Assert {


    @Test
    public void givenBasicConnectionPoolInstance_thenCorrect() throws Exception {
        DataSourceImpl manager = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);

        Connection connection = manager.getConnection();
        assertTrue(connection.isValid(1));
        manager.closeConnections();
    }

    @Test
    public void getConnection_thenCorrect() throws DataSourceException {
        DataSourceImpl manager = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);

        Connection connection = manager.getConnection();
        assertTrue(manager.close(connection));
        manager.closeConnections();

    }

    @Test
    public void givenBasicConnectionPoolInstance_whenShutdown_thenEmpty() throws Exception {
        DataSourceImpl manager = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);

        ConnectionPool connectionPool = manager.getConnectionPool();
        assertEquals(20, (int) connectionPool.availableConnectionsAmount());
        manager.closeConnections();
        assertEquals(0, (int) connectionPool.availableConnectionsAmount());
    }


}
