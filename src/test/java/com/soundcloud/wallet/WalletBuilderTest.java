package com.soundcloud.wallet;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.TestUtils;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DataSource;
import com.soundcloud.dao.DataSourceImpl;
import com.soundcloud.dao.TransactionManager;
import com.soundcloud.dao.TransactionManagerImpl;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.dao.TransactionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.*;

import static com.soundcloud.TestUtils.createDB;

@RunWith(JUnit4.class)
public class WalletBuilderTest {
    private static final String SELECT_ALL_QUERY = "select wallet_id, amount from soundcloud_test.user_wallet";
    private static final String INSERT_QUERY = "insert into soundcloud_test.user_wallet ( amount) values (?)";
    private Wallet wallet;
    private ConnectionManager connectionManager;

    @Before
    public void init() throws SQLException, TransactionException, ConnectionManagerException {
        DataSource dataSource = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = Mockito.spy(new TransactionManagerImpl(dataSource));
        transactionManager.beginTransaction();
        connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        Connection connection = connectionManager.getConnection();
        createDB(connection);
        transactionManager.commitTransaction();
        wallet = new Wallet(1L, new BigDecimal(1));
        save();
    }

    @Test
    public void save() throws ConnectionManagerException, SQLException {
        Connection connection = connectionManager.getConnection();
        PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        int i = 0;
        saveStatement.setBigDecimal(++i, wallet.getAmount());
        saveStatement.executeUpdate();
        ResultSet generatedKeys = saveStatement.getGeneratedKeys();
        Assert.assertTrue(generatedKeys.next());

    }


    @Test
    public void isBuildRight() throws SQLException, ConnectionManagerException, BuildException {
        EntityBuilder<Wallet> builder = new WalletBuilder();
        Wallet Test = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Test = builder.build(resultSet);
            }
        }
        assert Test != null;
        Assert.assertEquals(wallet.getAmount(), Test.getAmount());
    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }
}
