package com.soundcloud.user;

import com.soundcloud.bean.Bean;
import com.soundcloud.bean.BeanQualifier;
import com.soundcloud.builder.BuildException;
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

@Bean(beanName = "UserDao")
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);
    private static final String SELECT_FOLLOWERS_QUERY =
            "select user_follow_user.user_id, user_account.user_id, user_account.user_login, user_account.user_password, user_account.user_email,user_account.user_follows,user_account.user_followers, user_account.wallet_id, user_account.user_icon from soundcloud.user_follow_user " +
                    "join soundcloud.user_account on user_follow_user.user_id = soundcloud.user_account.user_id " +
                    "where user_follow_user.user_follow_id=?";

    private static final String SELECT_FOLLOWS_QUERY = "select user_follow_user.user_follow_id, user_account.user_id, user_account.user_login, user_account.user_password, user_account.user_email,user_account.user_follows,user_account.user_followers, user_account.wallet_id, user_account.user_icon  from soundcloud.user_follow_user " +
            "join soundcloud.user_account on user_follow_user.user_follow_id = soundcloud.user_account.user_id " +
            "where user_follow_user.user_id=?";
    private static final String SELECT_NOT_FOLLOWS_QUERY = "select user_follow_id from soundcloud.user_follow_user " +
            "where user_id=?";

    private static final String SELET_ALL_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon from soundcloud.user_account ";

    private static final String SELECT_ALL_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon from soundcloud.user_account";
    private static final String SELECT_BY_ID_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id , user_icon from soundcloud.user_account where user_id = ?";
    private static final String SELECT_BY_LOGIN_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon from soundcloud.user_account where user_login = ?";
    private static final String SELECT_BY_NAME_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon from soundcloud.user_account where user_login COLLATE UTF8_GENERAL_CI like ?";

    private static final String INSERT_QUERY = "insert into soundcloud.user_account (user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon) values (?,?,?,?,?,?,?)";
    private static final String INSERT_FOLLOW_USER_QUERY = "insert into soundcloud.user_follow_user ( user_id,user_follow_id) values (?,?)";


    private static final String UPDATE_QUERY = "update soundcloud.user_account set user_login=?, user_password=?, user_email=?,user_follows=?,user_followers=?, wallet_id=?, user_icon=?  where user_id = ?";
    private static final String UPDATE_FOLLOW_USER_AMOUNT_QUERY = "update soundcloud.user_account set user_follows=?  where user_id = ?";
    private static final String UPDATE_FOLLOWER_USER_AMOUNT_QUERY = "update soundcloud.user_account set user_followers=?  where user_id = ?";

    private static final String DELETE_FOLLOW_USER_QUERY = "delete from soundcloud.user_follow_user where user_id=? and user_follow_id=?";
    private static final String DELETE_QUERY = "delete from soundcloud.user_account where user_id = ?";
    private final EntityBuilder<User> entityBuilder;
    private final ConnectionManager connectionManager;

    public UserDaoImpl(ConnectionManager connectionManager, @BeanQualifier(value = "UserBuilder") EntityBuilder<User> entityBuilder) {
        this.connectionManager = connectionManager;
        this.entityBuilder = entityBuilder;
    }


    @Override
    public Optional<User> findByLogin(String login) throws DaoException, SQLException {
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByLoginStatement = connection.prepareStatement(SELECT_BY_LOGIN_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByLoginStatement.setString(1, login);
            resultSet = findByLoginStatement.executeQuery();

            if (resultSet.next()) {
                return build(resultSet);
            }
            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    @Override
    public Optional<User> findByName(String login) throws DaoException, SQLException {
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByLoginStatement = connection.prepareStatement(SELECT_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByLoginStatement.setString(1, "%"+login+"%");
            resultSet = findByLoginStatement.executeQuery();
            if (resultSet.next()) {
                return build(resultSet);
            }
            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }


    @Override
    public List<User> getFollowerUsers(Long userId) throws DaoException {
        List<User> users = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_FOLLOWERS_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            int i = 0;
            getAllStatement.setLong(++i, userId);
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<User> user = build(resultSet);
                user.ifPresent(users::add);
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public List<User> getFollowUsers(Long userId) throws DaoException {
        List<User> users = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_FOLLOWS_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            int i = 0;
            getAllStatement.setLong(++i, userId);
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<User> user = build(resultSet);
                user.ifPresent(users::add);
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public boolean unfollowUser(Long userId, Integer followsAmount, Long followUserId, Integer followersAmount) throws DaoException {
        updateFollowAmount(userId, Long.valueOf(--followsAmount));
        updateFollowerAmount(followUserId, Long.valueOf(--followersAmount));
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(DELETE_FOLLOW_USER_QUERY)) {
            int i = 0;
            saveStatement.setLong(++i, userId);
            saveStatement.setLong(++i, followUserId);
            return saveStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public boolean followUser(Long userId, Long followsAmount, Long followUserId, Long followersAmount) throws DaoException {
        updateFollowAmount(userId, ++followsAmount);
        updateFollowerAmount(followUserId, ++followersAmount);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_FOLLOW_USER_QUERY)) {
            int i = 0;
            saveStatement.setLong(++i, userId);
            saveStatement.setLong(++i, followUserId);

            return saveStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public List<User> getNotFollowUsers(Long userId) throws DaoException {
        List<User> users = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_NOT_FOLLOWS_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            int i = 0;
            getAllStatement.setLong(++i, userId);
            List<Long> followUsersId = new LinkedList<>();
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                followUsersId.add(resultSet.getLong(UserField.USER_FOLLOW_ID.getField()));
            }
            List<User> userList = getAll();
            for (User user :
                    userList) {
                if (!user.getId().equals(userId) && !followUsersId.contains(user.getId())) {
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    private void updateFollowAmount(Long userId, Long follow) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(UPDATE_FOLLOW_USER_AMOUNT_QUERY)) {
            int i = 0;
            saveStatement.setLong(++i, follow);
            saveStatement.setLong(++i, userId);
            saveStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    private void updateFollowerAmount(Long userId, Long follower) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(UPDATE_FOLLOWER_USER_AMOUNT_QUERY)) {
            int i = 0;
            saveStatement.setLong(++i, follower);
            saveStatement.setLong(++i, userId);
            saveStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public boolean delete(Long key) throws DaoException {
        return deleteUser(key, connectionManager);
    }

    private boolean deleteUser(Long key, ConnectionManager connectionManager) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(UserDaoImpl.DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1, key);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            UserDaoImpl.LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            UserDaoImpl.LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public boolean update(Long key, User user) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            i = setStatement(user, updateStatement, i);
            updateStatement.setLong(++i, key);
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
    public Long save(User user) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(user, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    private int setStatement(User user, PreparedStatement statement, int i) throws SQLException {
        statement.setString(++i, user.getLogin());
        statement.setString(++i, user.getPassword());
        statement.setString(++i, user.getEmail());
        statement.setInt(++i, user.getUserFollowers());
        statement.setInt(++i, user.getUserFollows());
        statement.setLong(++i, user.getWalletId());
        statement.setBlob(++i, user.getUserIcon());
        return i;

    }

    @Override
    public Optional<User> getById(Long id) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            return build(resultSet);
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public List<User> getAll() throws DaoException {
        List<User> users = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<User> user = build(resultSet);
                user.ifPresent(users::add);
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public User clone(User user) {
        return new User(user);
    }

    @Override
    public Optional<User> build(ResultSet data) throws BuildException, DaoException {
        try {
            User user = entityBuilder.build(data);
            return Optional.of(user);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }


}
