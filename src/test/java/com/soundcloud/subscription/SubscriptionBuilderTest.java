package com.soundcloud.subscription;

import com.soundcloud.TestUtils;
import com.soundcloud.album.AlbumBuilderTest;
import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DaoQueryExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.*;
import java.time.LocalDate;

import static com.soundcloud.TestUtils.createDB;
import static com.soundcloud.user.UserDaoTest.SAVE_EXCEPTION;

@RunWith(JUnit4.class)
public class SubscriptionBuilderTest{
    private static final String SELECT_ALL_QUERY = "select subscription_user_id,subscription_id,subscription_date, subscription_status from soundcloud_test.subscription";
    private static final String INSERT_QUERY = "insert into soundcloud_test.subscription ( subscription_user_id, subscription_date, subscription_status ) values (?,?,?)";
    private static final Logger LOGGER = LogManager.getLogger(AlbumBuilderTest.class);
    private Subscription subscription;
    private ConnectionManager connectionManager;

    public void save() throws DaoQueryExecuteException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(subscription, saveStatement, i);
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

    private void setStatement(Subscription subscription, PreparedStatement statement, int i) throws SQLException {
        statement.setLong(++i, subscription.getUserId());
        statement.setDate(++i, java.sql.Date.valueOf(subscription.getSubscriptionDate()));
        statement.setString(++i, subscription.getSubscriptionStatus().getField());

    }

    @Before
    public void init() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {

        ApplicationContext.initialize();
        connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();

        createDB(connection);
        subscription = new Subscription(0L, LocalDate.now(),0L, SubscriptionStatus.ACTIVE);
        save();


    }

    @Test
    public void isBuildRight() throws SQLException, ConnectionManagerException, BuildException {
        EntityBuilder<Subscription> builder = new SubscriptionBuilder();
        Subscription subscription = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                subscription = builder.build(resultSet);
            }
            Assert.assertEquals(this.subscription.getSubscriptionId(),subscription.getSubscriptionId());
            Assert.assertEquals(this.subscription.getUserId(),subscription.getUserId());
            Assert.assertEquals(this.subscription.getSubscriptionStatus(),subscription.getSubscriptionStatus());

        }


    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }
}