package com.soundcloud.dao;

import java.sql.Connection;

/**
 * Interface for working with connection pool
 */
public interface DataSource {
    /**
     * method get connection from connection pool
     *
     * @return sql connection
     * @throws DataSourceException if we have exception while getting connection
     */
    Connection getConnection() throws DataSourceException;

    /**
     * method close all connections in connection pool
     *
     * @throws DataSourceException if we have exception while closing connections
     */
    void closeConnections() throws DataSourceException;

    /**
     * method return connection to connection pool
     *
     * @param connection sql connection, that we have been used
     * @return if we return connection successfully, then return true, else false
     * @throws DataSourceException if we have exception while closing connection
     */
    boolean close(Connection connection) throws DataSourceException;
}
