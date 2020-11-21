package com.soundcloud.connection;

import java.sql.Connection;

/**
 * Connection manager for jdbc
 */
public interface ConnectionManager {
    /**
     * method return connection for mysql database
     *
     * @return connection for mysql database
     * @throws ConnectionManagerException if Service exception has thrown
     */
    Connection getConnection() throws ConnectionManagerException;
}
