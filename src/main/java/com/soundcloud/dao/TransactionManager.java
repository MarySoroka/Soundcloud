package com.soundcloud.dao;

import java.sql.Connection;

/**
 * Interface for working with transactions
 */
public interface TransactionManager {

    /**
     * method begin transaction
     *
     * @throws TransactionException if we have some troubles with transaction
     */
    void beginTransaction() throws TransactionException;

    /**
     * method commit transaction
     *
     * @throws TransactionException if we have some troubles with transaction
     */

    void commitTransaction() throws TransactionException;

    /**
     * method rollback transaction
     *
     * @throws TransactionException if we have some troubles with transaction
     */
    void rollbackTransaction() throws TransactionException;

    /**
     * method return connection from connection pool
     *
     * @return sql connection
     * @throws TransactionException if we have troubles with sql request
     */
    Connection getConnection() throws TransactionException;
}
