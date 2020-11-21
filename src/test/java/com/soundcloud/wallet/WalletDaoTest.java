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
import java.util.LinkedList;
import java.util.List;

import static com.soundcloud.TestUtils.createDB;

@RunWith(JUnit4.class)
public class WalletDaoTest {
    private static final String SELECT_ALL_QUERY = "select wallet_id, amount from soundcloud_test.user_wallet";
    private static final String INSERT_QUERY = "insert into soundcloud_test.user_wallet ( amount) values (?)";
    private static final String SELECT_BY_ID_QUERY = "select wallet_id, amount from soundcloud_test.user_wallet where wallet_id = ?";
    private static final String UPDATE_QUERY = "update soundcloud_test.user_wallet set amount=? where wallet_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud_test.user_wallet where wallet_id = ?";
    private final EntityBuilder<Wallet> entityBuilder = new WalletBuilder();
    private final Wallet wallet = new Wallet(0L, new BigDecimal(1));
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

    }


    @Test
    public void deleteWalletByIdIsRight() throws SQLException, ConnectionManagerException {
        Long id = saveWallet();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, id);
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }

    @Test
    public void updateWalletByIdIsRight() throws SQLException, ConnectionManagerException {
        Long id = saveWallet();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            updateStatement.setBigDecimal(++i, wallet.getAmount());
            updateStatement.setLong(++i, id);
            boolean isUpdate = updateStatement.executeUpdate() > 0;
            Assert.assertTrue(isUpdate);
        }

    }

    private Long saveWallet() throws ConnectionManagerException, SQLException {
        Connection connection = connectionManager.getConnection();
        PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        int i = 0;
        saveStatement.setBigDecimal(++i, wallet.getAmount());
        saveStatement.executeUpdate();
        ResultSet generatedKeys = saveStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getLong(1);
    }

    @Test
    public void saveWalletByIdIsRight() throws ConnectionManagerException, SQLException {
        Connection connection = connectionManager.getConnection();
        PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        int i = 0;
        saveStatement.setBigDecimal(++i, wallet.getAmount());
        saveStatement.executeUpdate();
        ResultSet generatedKeys = saveStatement.getGeneratedKeys();
        Assert.assertTrue(generatedKeys.next());
    }

    @Test
    public void getWalletByIdIsRight() throws SQLException, ConnectionManagerException {
        Long id = saveWallet();
        ResultSet resultSet = null;
        Connection connection = connectionManager.getConnection();
        PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
        findByIdStatement.setLong(1, id);
        resultSet = findByIdStatement.executeQuery();
        Assert.assertTrue(resultSet.next());
    }

    @Test
    public void getAllWalletIsRight() throws ConnectionManagerException, SQLException, BuildException {
        Long id = saveWallet();
        List<Wallet> wallets = new LinkedList<>();
        Connection connection = connectionManager.getConnection();
        PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
        ResultSet resultSet = getAllStatement.executeQuery();
        while (resultSet.next()) {
            Wallet wallet = entityBuilder.build(resultSet);
            wallets.add(wallet);
        }
        Assert.assertEquals(1, wallets.size());
    }

    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }

}
