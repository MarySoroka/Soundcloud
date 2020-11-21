package com.soundcloud.subscription;

import com.soundcloud.bean.Bean;
import com.soundcloud.bean.BeanQualifier;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.DaoQueryExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "subscriptionDao")
public class SubscriptionDaoImpl  implements  SubscriptionDao{
    private static final Logger LOGGER = LogManager.getLogger(SubscriptionDaoImpl.class);
    private static final String SELECT_BY_USER_ID_QUERY = "select subscription_id, subscription_user_id,subscription_date, subscription_status from soundcloud.subscription where subscription_user_id = ?";
    private static final String SELECT_ALL_QUERY = "select subscription_id, subscription_user_id,subscription_date, subscription_status from soundcloud.subscription";
    private static final String SELECT_BY_ID_QUERY = "select subscription_id, subscription_user_id, subscription_date, subscription_status from soundcloud.subscription where subscription_id = ?";
    private static final String INSERT_QUERY = "insert into soundcloud.subscription (subscription_user_id,subscription_date, subscription_status) values (?,?,?)";
    private static final String UPDATE_QUERY = "update soundcloud.subscription set subscription_user_id=?, subscription_date=?, subscription_status=?  where subscription_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud.subscription where subscription_id = ?";


    private final EntityBuilder<Subscription> entityBuilder;
    private final ConnectionManager connectionManager;

    public SubscriptionDaoImpl(ConnectionManager connectionManager, @BeanQualifier(value = "subscriptionBuilder") EntityBuilder<Subscription> entityBuilder) {
        this.connectionManager = connectionManager;
        this.entityBuilder = entityBuilder;
    }


    @Override
    public boolean delete(Long key) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, key);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        }
    }


    @Override
    public boolean update(Long key, Subscription subscription) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            i = setStatement(subscription, updateStatement, i);
            updateStatement.setLong(++i, subscription.getSubscriptionId());
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    @Override
    public Long save(Subscription subscription) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(subscription,saveStatement,i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    private int setStatement(Subscription subscription, PreparedStatement statement, int i) throws SQLException {
        statement.setLong(++i, subscription.getUserId());
        statement.setDate(++i, java.sql.Date.valueOf(subscription.getSubscriptionDate()));
        statement.setString(++i,subscription.getSubscriptionStatus().getField());
        return i;
    }

    @Override
    public Optional<Subscription> getById(Long id) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            return build(resultSet);
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public List<Subscription> getAll() throws DaoException {
        List<Subscription> subscriptions = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Subscription> subscription = build(resultSet);
                subscription.ifPresent(subscriptions::add);
            }
            return subscriptions;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }


    @Override
    public Subscription clone(Subscription subscription) {
        return new Subscription(subscription);
    }

    @Override
    public Optional<Subscription> build(ResultSet data) throws DaoException {
        try {
            Subscription subscription = entityBuilder.build(data);
            return Optional.of(subscription);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }


    @Override
    public Optional<Subscription> getSubscriptionByUserId(Long userId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, userId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            if (resultSet.next()){
                return build(resultSet);
            }else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }
}
