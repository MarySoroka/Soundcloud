package com.soundcloud.connection;

import com.soundcloud.bean.Bean;
import com.soundcloud.dao.DataSource;
import com.soundcloud.dao.TransactionManager;
import com.soundcloud.dao.DataSourceException;
import com.soundcloud.dao.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

import static com.soundcloud.application.ApplicationConstants.DATA_SOURCE_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.TRANSACTION_EXCEPTION;

/**
 * Connection manager implementation
 */
@Bean(beanName = "ConnectionManager")
public class ConnectionManagerImpl implements ConnectionManager {


    private final TransactionManager transactionManager;
    private final DataSource dataSource;
    private static final Logger LOGGER = LogManager.getLogger(ConnectionManagerImpl.class);

    public ConnectionManagerImpl(TransactionManager transactionManager, DataSource dataSource) {
        this.transactionManager = transactionManager;
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws ConnectionManagerException {
        try {
            Connection connection = transactionManager.getConnection();
            return connection != null ? connection : dataSource.getConnection();
        } catch (DataSourceException e) {
            LOGGER.error(DATA_SOURCE_EXCEPTION.replace("0",e.getMessage()));
            throw new ConnectionManagerException(DATA_SOURCE_EXCEPTION.replace("0",e.getMessage()),e);
        } catch (TransactionException e) {
            LOGGER.error(TRANSACTION_EXCEPTION.replace("0",e.getMessage()));
            throw new ConnectionManagerException(TRANSACTION_EXCEPTION.replace("0",e.getMessage()),e);
        }
    }
}
