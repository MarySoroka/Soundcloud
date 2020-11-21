package com.soundcloud.user;

import com.soundcloud.TestUtils;
import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.role.Role;
import com.soundcloud.role.RoleType;
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
import java.util.*;

import static com.soundcloud.TestUtils.createDB;

@RunWith(JUnit4.class)
public class UserDaoTest {
    public static final String EXECUTE_EXCEPTION = "Couldn't execute query 0";
    public static final String BUILD_EXCEPTION = "Couldn't build user 0";
    public static final String SAVE_EXCEPTION = "Couldn't save query 0";
    private static final String SELECT_ALL_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id, user_icon from soundcloud_test.user_account";
    private static final String SELECT_BY_ID_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id from soundcloud_test.user_account where user_id = ?";
    private static final String SELECT_BY_LOGIN_QUERY = "select user_id, user_login, user_password, user_email,user_follows,user_followers, wallet_id from soundcloud_test.user_account where user_login = ? ";
    private static final String INSERT_QUERY = "insert into soundcloud_test.user_account ( user_login, user_password, user_email,user_follows,user_followers, wallet_id, USER_ICON) values (?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "update soundcloud_test.user_account set user_login=?, user_password=?, user_email=?,user_follows=?,user_followers=?, wallet_id=?, user_icon=?  where user_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud_test.user_account where user_id = ?";
    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);
    private ConnectionManager connectionManager;
    private UserDto userDTO;

    @Before
    public void init() throws SQLException, TransactionException, ConnectionManagerException, IOException {
        DataSource dataSource = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = Mockito.spy(new TransactionManagerImpl(dataSource));
        transactionManager.beginTransaction();
        connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        Connection connection = connectionManager.getConnection();
        createDB(connection);
        transactionManager.commitTransaction();

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.UNSIGNED_USER, 1L, 1L));
        User user = new User("flash",
                "123",
                "m@gmail.com", 0, 0);
        user.setId(1L);
        userDTO = new UserDto(user, roles);

    }

    @Test
    public void saveUserIsRight() throws DaoQueryExecuteException, ConnectionManagerException {
        Assert.assertNotNull(save());
    }

    private int setStatement(UserDto userDTO, PreparedStatement statement) throws SQLException {
        int i = 0;
        statement.setString(++i, userDTO.getLogin());
        statement.setString(++i, userDTO.getPassword());
        statement.setString(++i, userDTO.getEmail());
        statement.setInt(++i, userDTO.getUserFollowers());
        statement.setInt(++i, userDTO.getUserFollows());
        statement.setLong(++i, 0L);
        statement.setBlob(++i, this.getClass().getResourceAsStream("/avatar.png"));
        return i;
    }

    @Test
    public void updateUserIsRight() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {
        Long id = save();
        userDTO.setEmail("mary@h.com");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = setStatement(userDTO, updateStatement);
            updateStatement.setLong(++i, id);
            boolean isUpdate = updateStatement.executeUpdate() > 0;
            Assert.assertTrue(isUpdate);
        }

    }


    @Test
    public void getById() throws DaoQueryExecuteException, SQLException, ConnectionManagerException {
        Long id = save();
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, id);
            resultSet = findByIdStatement.executeQuery();
            Assert.assertTrue(resultSet.next());

        } catch (SQLException e) {
            LOGGER.error(EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(EXECUTE_EXCEPTION.replace("0", ""), e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    @Test
    public void getAll() throws DaoQueryExecuteException, ConnectionManagerException, IOException, SQLException, BuildException {
        userDTO.setEmail("fff@gmail.com");
        save();
        List<UserDto> userDtos = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<UserDto> user = Optional.of(new UserDto(buildUser(resultSet).get(), 1L));
                user.ifPresent(userDtos::add);
            }
            Assert.assertEquals(1, userDtos.size());
        }
    }


    private Optional<User> buildUser(ResultSet resultSet) throws BuildException {
        try {
            EntityBuilder<User> entityBuilder = new UserBuilder();
            User user = entityBuilder.build(resultSet);
            return Optional.of(user);
        } catch (SQLException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new BuildException(BUILD_EXCEPTION.replace("0", ""), e);
        }
    }

    private Long save() throws DaoQueryExecuteException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(userDTO, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            Long id = generatedKeys.getLong(1);
            LOGGER.info("id {}", id);
            return id;
        } catch (SQLException e) {
            LOGGER.error(SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Test
    public void delete() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {
        Long id = save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, id);
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }

    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }

}
