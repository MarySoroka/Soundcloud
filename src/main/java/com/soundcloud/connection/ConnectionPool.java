package com.soundcloud.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection manager interface for database interaction
 */
public interface ConnectionPool {

    /**
     * method get database connection
     *
     * @return database connection
     */
    Connection getConnection();

    /**
     * method release database connection and return it to connection pool
     *
     * @return if we release successfully, then return true, else false
     */
    boolean releaseConnection();

    /**
     * method close all connection in connection pool
     *
     * @throws SQLException         if we have exception in sql request
     * @throws InterruptedException if we have exception with thread
     */
    void closeConnections() throws SQLException, InterruptedException;

    /**
     * method shows amount of available connections in connection pool
     *
     * @return amount of available connections
     */
    Integer availableConnectionsAmount();
}
