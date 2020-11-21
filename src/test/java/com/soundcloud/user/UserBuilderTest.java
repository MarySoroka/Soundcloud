package com.soundcloud.user;

import com.soundcloud.TestUtils;
import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.soundcloud.TestUtils.createDB;
import static com.soundcloud.user.UserDaoTest.SAVE_EXCEPTION;

@RunWith(JUnit4.class)
public class UserBuilderTest {
    private static final String SELECT_ALL_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon from soundcloud_test.user_account";
    private static final String INSERT_QUERY = "insert into soundcloud_test.user_account ( user_login, user_password, user_email,user_follows,user_followers, wallet_id, USER_ICON) values (?,?,?,?,?,?,?)";
    private static final Logger LOGGER = LogManager.getLogger(UserBuilderTest.class);
    private final Map<String, String> data = new HashMap<>();
    private User user;
    private ConnectionManager connectionManager;

    public void save() throws DaoQueryExecuteException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(user, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            Long id = generatedKeys.getLong(1);
            LOGGER.info("id {}", id);
            Assert.assertNotNull(id);
        } catch (SQLException | ConnectionManagerException e) {
            LOGGER.error(SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    private void setStatement(User user, PreparedStatement statement) throws SQLException {
        int i = 0;
        statement.setString(++i, user.getLogin());
        statement.setString(++i, user.getPassword());
        statement.setString(++i, user.getEmail());
        statement.setInt(++i, user.getUserFollowers());
        statement.setInt(++i, user.getUserFollows());
        statement.setLong(++i, 1);
        statement.setBlob(++i, this.getClass().getResourceAsStream("/avatar.png"));
    }

    @Before
    public void init() throws SQLException, DaoQueryExecuteException, TransactionException, ConnectionManagerException {
        DataSource dataSource = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = Mockito.spy(new TransactionManagerImpl(dataSource));
        transactionManager.beginTransaction();
        connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        Connection connection = connectionManager.getConnection();
        createDB(connection);
        transactionManager.commitTransaction();


        user = new User(1L,
                "flash",
                "123",
                "m@gmail.com",
                this.getClass().getResourceAsStream("/avatar.png"),
                12,
                15, 1L);
        save();


    }

    @Test
    public void isBuildRight() throws SQLException, IOException, ConnectionManagerException, BuildException {
        EntityBuilder<User> builder = new UserBuilder();
        User Test = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Test = builder.build(resultSet);
            }
        }
        Assert.assertEquals(user.getEmail(), Test.getEmail());
        Assert.assertEquals(user.getLogin(), Test.getLogin());
        Assert.assertEquals(user.getPassword(), Test.getPassword());
        Assert.assertEquals(user.getUserIcon().available(), Test.getUserIcon().available());

    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }

}
