package com.soundcloud.subscription;

import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.TestUtils.createDB;

@RunWith(JUnit4.class)
public class SubscriptionDaoTest {
    private static final String SELECT_BY_USER_ID_QUERY = "select subscription_id, subscription_user_id,subscription_date, subscription_status from soundcloud_test.subscription where subscription_user_id = ?";
    private static final String SELECT_ALL_QUERY = "select subscription_id, subscription_user_id,subscription_date, subscription_status from soundcloud_test.subscription";
    private static final String SELECT_BY_ID_QUERY = "select subscription_id, subscription_user_id, subscription_date, subscription_status from soundcloud_test.subscription where subscription_id = ?";
    private static final String INSERT_QUERY = "insert into soundcloud_test.subscription (subscription_user_id,subscription_date, subscription_status) values (?,?,?)";
    private static final String UPDATE_QUERY = "update soundcloud_test.subscription set subscription_user_id=?, subscription_date=? , subscription_status=? where subscription_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud_test.subscription where subscription_id = ?";

    private Subscription subscription;
    private final ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);


    @BeforeClass
    public static void init() throws SQLException, ConnectionManagerException {
        ApplicationContext.initialize();
        ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();
        createDB(connection);


    }

    @Before
    public void initSubscription() throws SQLException, ConnectionManagerException {
        subscription = new Subscription(0L, LocalDate.now(), 0L, SubscriptionStatus.ACTIVE);
        save();


    }

    @Test
    public void deleteSubscription_isRight() throws ConnectionManagerException, SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, subscription.getSubscriptionId());
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }


    @Test
    public void updateSubscription_isRight() throws SQLException, ConnectionManagerException {
        Long id = save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            i = setStatement(subscription, updateStatement, i);
            updateStatement.setLong(++i, id);
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }

    }

    public Long save() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(subscription, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong(1);
        }

    }

    private int setStatement(Subscription subscription, PreparedStatement statement, int i) throws SQLException {
        statement.setLong(++i, subscription.getUserId());
        statement.setDate(++i, java.sql.Date.valueOf(subscription.getSubscriptionDate()));
        statement.setString(++i, subscription.getSubscriptionStatus().getField());
        return i;

    }

    @Test
    public void getByIdSubscription_isRight() throws BuildException, SQLException, ConnectionManagerException {
        Long id = save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            Assert.assertTrue(build(resultSet).isPresent());
        }
    }

    @Test
    public void getAllSubscription_isRight() throws SQLException, ConnectionManagerException, BuildException {

        List<Subscription> subscriptions = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Subscription> subscription = build(resultSet);
                subscription.ifPresent(subscriptions::add);
            }
            Assert.assertEquals(1, subscriptions.size());
        }
    }


    public Optional<Subscription> build(ResultSet data) throws BuildException, SQLException {
        EntityBuilder<Subscription> entityBuilder = new SubscriptionBuilder();
        Subscription subscription = entityBuilder.build(data);
        return Optional.of(subscription);
    }

    @Test
    public void getSubscriptionByUserIdSubscription_isRight() throws SQLException, ConnectionManagerException, BuildException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, subscription.getUserId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            Assert.assertTrue(build(resultSet).isPresent());
        }

    }
}