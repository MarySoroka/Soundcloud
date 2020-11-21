package com.soundcloud.connection;

import com.soundcloud.bean.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.soundcloud.application.ApplicationConstants.CONNECTION_POOL_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.CONNECTION_POOL_FATAL_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.TAKE_CONNECTION_EXCEPTION;

/**
 * Class, that implements ConnectionPool and for work with connection pool
 */
@Bean(beanName = "ConnectionPool")
public class ConnectionPoolImpl implements ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolImpl.class);
    private final String databaseUser;
    private final String databasePassword;
    private final Integer poolCapacity;
    private final String databaseDriver;
    private final String databaseUrl;
    private final BlockingQueue<Connection> usedConnections;
    private final BlockingQueue<Connection> availableConnections;

    public ConnectionPoolImpl(String databaseUser, String databasePassword, Integer poolCapacity, String databaseDriver, String databaseUrl) {
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.poolCapacity = poolCapacity;
        this.databaseDriver = databaseDriver;
        this.databaseUrl = databaseUrl;
        this.usedConnections = new ArrayBlockingQueue<>(this.poolCapacity);
        this.availableConnections = new ArrayBlockingQueue<>(this.poolCapacity);
        initDriver();
        initConnectionPool();
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = availableConnections.take();
            usedConnections.put(connection);
            LOGGER.info("Get connection successfully");
            return createProxyConnection(connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(TAKE_CONNECTION_EXCEPTION.replace("0", e.getMessage()));
            throw new ConnectionPoolException(TAKE_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * method create connection with database
     *
     * @return database connection
     * @throws SQLException if we have exception in sql request
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(this.databaseUrl, this.databaseUser, this.databasePassword);
    }

    /**
     * method init connection pool for particular amount of connections
     */
    private void initConnectionPool() {
        try {
            for (int i = 0; i < this.poolCapacity; i++) {
                this.availableConnections.put(createConnection());
                LOGGER.info("Init connection {}", i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(CONNECTION_POOL_EXCEPTION.replace("0", e.getMessage()));
            throw new ConnectionPoolException(CONNECTION_POOL_EXCEPTION, e);
        } catch (SQLException e) {
            LOGGER.error(CONNECTION_POOL_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ConnectionPoolException(CONNECTION_POOL_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    /**
     * method init jdbc driver
     */
    private void initDriver() {
        try {
            Class.forName(this.databaseDriver);
            LOGGER.info("Init driver {}", this.databaseDriver);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Can't load driver {}", this.databaseDriver);
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean releaseConnection() {
        try {
            Connection connection = usedConnections.take();
            availableConnections.put(connection);
            LOGGER.info("Release connection");
            LOGGER.info("Amount of available connections: {}", this.availableConnections.size());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(CONNECTION_POOL_EXCEPTION.replace("0", e.getMessage()));
            throw new ConnectionPoolException(CONNECTION_POOL_EXCEPTION, e);
        }

    }

    @Override
    public void closeConnections() {
        try {
            if (!usedConnections.isEmpty()) {
                for (Connection connection : usedConnections) {
                    releaseConnection();
                }
            }
            for (int i = 0; i < poolCapacity; i++) {
                availableConnections.take().close();
                LOGGER.info("Close connection {}", i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(CONNECTION_POOL_EXCEPTION.replace("0", e.getMessage()));
            throw new ConnectionPoolException(CONNECTION_POOL_EXCEPTION, e);
        } catch (SQLException e) {
            LOGGER.error(CONNECTION_POOL_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ConnectionPoolException(CONNECTION_POOL_FATAL_EXCEPTION, e);
        }
    }

    @Override
    public Integer availableConnectionsAmount() {
        return this.poolCapacity - this.availableConnections.remainingCapacity();
    }

    /**
     * method create proxy connection
     *
     * @param connection database connection
     * @return proxy connection
     */
    private Connection createProxyConnection(Connection connection) {
        return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        releaseConnection();
                        return null;
                    } else if ("hashCode".equals(method.getName())) {
                        return connection.hashCode();
                    } else {
                        return method.invoke(connection, args);
                    }
                });
    }
}
