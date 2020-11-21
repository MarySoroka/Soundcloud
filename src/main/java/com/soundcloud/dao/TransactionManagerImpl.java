package com.soundcloud.dao;

import com.soundcloud.bean.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import static com.soundcloud.application.ApplicationConstants.TRANSACTION_EXCEPTION;

/**
 * Class, that implements TransactionalManager
 */
@Bean(beanName = "TransactionManager")
public class TransactionManagerImpl implements TransactionManager {
    private static final Logger LOGGER = LogManager.getLogger(TransactionManagerImpl.class);
    private final DataSource dataSource;
    private final ThreadLocal<Connection> localConnection = new ThreadLocal<>();

    public TransactionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void beginTransaction() throws TransactionException {
        try (Connection connection = dataSource.getConnection();) {
            connection.setAutoCommit(false);
            localConnection.set(connection);
        } catch (SQLException | DataSourceException e) {
            LOGGER.error(TRANSACTION_EXCEPTION.replace("0", e.getMessage()));
            throw new TransactionException(TRANSACTION_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public void commitTransaction() throws TransactionException {
        Connection connection = localConnection.get();
        try {
            dataSource.close(connection);
            if (connection != null) {
                connection.commit();
            } else {
                LOGGER.error("Can't commit connection");
            }
        } catch (SQLException | DataSourceException e) {
            LOGGER.error(TRANSACTION_EXCEPTION.replace("0", e.getMessage()));
            throw new TransactionException(TRANSACTION_EXCEPTION.replace("0", e.getMessage()), e);
        } finally {
            localConnection.remove();
        }

    }

    @Override
    public void rollbackTransaction() throws TransactionException {
        Connection connection = localConnection.get();
        try {
            if (connection != null) {
                connection.rollback();
                dataSource.close(connection);
            } else {
                LOGGER.error("Can't rollback connection");
            }
        } catch (SQLException | DataSourceException e) {
            LOGGER.error(TRANSACTION_EXCEPTION.replace("0", e.getMessage()));
            throw new TransactionException(TRANSACTION_EXCEPTION.replace("0", e.getMessage()), e);
        } finally {
            localConnection.remove();
        }
    }

    @Override
    public Connection getConnection() throws TransactionException {
        try {
            return this.dataSource.getConnection();
        } catch (DataSourceException e) {
            LOGGER.error(TRANSACTION_EXCEPTION.replace("0", e.getMessage()));
            throw new TransactionException(TRANSACTION_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
