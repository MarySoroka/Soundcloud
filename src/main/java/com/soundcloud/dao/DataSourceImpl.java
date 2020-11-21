package com.soundcloud.dao;

import com.soundcloud.bean.Bean;
import com.soundcloud.connection.ConnectionPool;
import com.soundcloud.connection.ConnectionPoolImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.soundcloud.application.ApplicationConstants.*;

/**
 * Class, that implements DataSource
 */
@Bean(beanName = "DataSource")
public class DataSourceImpl implements DataSource {

    private static final Logger LOGGER = LogManager.getLogger(DataSourceImpl.class);
    private static DataSourceImpl instance;
    private static Lock lock = new ReentrantLock();
    private final ConnectionPool connectionPool;

    private DataSourceImpl(String properties) {
        try {
            this.connectionPool = createPool(properties);
        } catch (DataSourceException e) {
            LOGGER.error(DATA_SOURCE_EXCEPTION.replace("0", e.getMessage()));
            throw new IllegalStateException(DATA_SOURCE_EXCEPTION.replace("0", e.getMessage()));
        }
    }

    public static DataSourceImpl getInstance(String databaseProperties) {
        lock.lock();
        try {
            if (instance == null) {
                instance = new DataSourceImpl(databaseProperties);
            }
        } finally {
            lock.unlock();
        }
        return instance;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    /**
     * method load properties from property file
     *
     * @param propertyFile property file
     * @return properties
     * @throws DataSourceException if we have troubles with loading
     */
    private Properties loadProperties(String propertyFile) throws DataSourceException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
            Properties data = new Properties();
            if (inputStream != null) {
                data.load(inputStream);
                return data;
            } else {
                LOGGER.error(PROPERTY_FILE_NOT_FOUND_EXCEPTION.replace("0", propertyFile));
                throw new FileNotFoundException(PROPERTY_FILE_NOT_FOUND_EXCEPTION.replace("0", propertyFile));
            }
        } catch (IOException e) {
            LOGGER.error(CONNECTION_POOL_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new DataSourceException(CONNECTION_POOL_FATAL_EXCEPTION, e);
        }
    }

    /**
     * method create connection pool
     *
     * @param databaseProperties database properties
     * @return connection pool
     * @throws DataSourceException if we have some trouble while creating connection pool
     */
    public ConnectionPool createPool(String databaseProperties) throws DataSourceException {
        lock.lock();
        try {
            Properties data = loadProperties(databaseProperties);
            return new ConnectionPoolImpl(data.getProperty("dataSource.user"),
                    data.getProperty("dataSource.password"),
                    Integer.parseInt(data.getProperty("dataSource.poolCapacity")),
                    data.getProperty("dataSourceClassName"),
                    data.getProperty("dataSource.url"));
        } finally {
            lock.unlock();
        }
    }


    @Override
    public Connection getConnection() {
        lock.lock();
        try {
            return this.connectionPool.getConnection();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void closeConnections() throws DataSourceException {
        lock.lock();
        try {
            connectionPool.closeConnections();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(TAKE_CONNECTION_EXCEPTION.replace("0", e.getMessage()));
            throw new DataSourceException(TAKE_CONNECTION_EXCEPTION, e);
        } catch (SQLException e) {
            LOGGER.error(CONNECTION_POOL_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new DataSourceException(CONNECTION_POOL_FATAL_EXCEPTION, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean close(Connection connection) {
        lock.lock();
        try {
            return connectionPool.releaseConnection();
        } finally {
            lock.unlock();
        }
    }
}
