package com.soundcloud.role;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.TestUtils;
import com.soundcloud.builder.BuildException;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.*;
import static com.soundcloud.application.ApplicationConstants.DAO_DELETE_EXCEPTION;
import static com.soundcloud.TestUtils.createDB;
@RunWith(JUnit4.class)
public class RoleDaoTest {
    public static final String EXECUTE_EXCEPTION = "Couldn't execute query 0";
    public static final String BUILD_EXCEPTION = "Couldn't build role 0";
    public static final String SAVE_EXCEPTION = "Couldn't save query 0";
    private static final String SELECT_ALL_QUERY = "select role_id,role_name, user_id from soundcloud_test.user_role join soundcloud_test.user_role_has_user on role_id = role_id";
    private static final String SELECT_BY_ID_QUERY =  "select role_id,role_name, user_id from soundcloud_test.user_role join soundcloud_test.user_role_has_user on role_id = role_id where role_id = ? ";
    private static final String INSERT_ROLE_QUERY = "INSERT INTO soundcloud_test.user_role (role_name) VALUES(?);";
    private static final String INSERT_ROLE_RELATION_QUERY = "INSERT INTO soundcloud_test.user_role_has_user (user_id, role_id) VALUES(?,?);";
    private static final String UPDATE_ROLE_QUERY = "update soundcloud_test.user_role set role_name=?  where role_id = ?";
    private static final String UPDATE_ROLE_RELATION_QUERY = "update soundcloud_test.user_role_has_user set user_id=?  where role_id = ?";
    private static final String DELETE_ROLE_QUERY = "delete from soundcloud_test.user_role where role_id = ?";
    private static final String DELETE_ROLE_RELATION_QUERY = "delete from soundcloud_test.user_role_has_user where role_id = ?";
    private static final Logger LOGGER = LogManager.getLogger(RoleDaoImpl.class);
    private ConnectionManager connectionManager;
    private Role role;

    @Before
    public void init() throws SQLException, TransactionException, ConnectionManagerException {
        DataSource dataSource = DataSourceImpl.getInstance(ApplicationConstants.DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = Mockito.spy(new TransactionManagerImpl(dataSource));
        transactionManager.beginTransaction();
        connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        Connection connection = connectionManager.getConnection();
        createDB(connection);
        transactionManager.commitTransaction();

        role = new Role(RoleType.ADMIN, 1L, 1L);
    }

    private Long saveRole() throws DaoQueryExecuteException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            saveStatement.setString(++i, role.getName().getType());
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


    private Long saveRoleRelation(Long id) throws DaoQueryExecuteException, ConnectionManagerException {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_ROLE_RELATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            saveStatement.setLong(++i, role.getUserId());
            saveStatement.setLong(++i, id);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            id = generatedKeys.getLong(1);
            LOGGER.info("id {}", id);
            return id;
        } catch (SQLException e) {
            LOGGER.error(SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Test
    public void updateUserIsRight() throws SQLException, ConnectionManagerException, DaoQueryExecuteException {

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ROLE_RELATION_QUERY)) {
            int i = 0;
            updateStatement.setLong(++i, role.getUserId());
            updateStatement.setLong(++i, role.getUserId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }

    }
    @Test
    public void assignRoleType() throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement assignStatement = connection.prepareStatement(INSERT_ROLE_QUERY,Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            assignStatement.setString(++i, role.getName().getType());
            assignStatement.executeUpdate();
            ResultSet generatedKeys = assignStatement.getGeneratedKeys();
            Assert.assertTrue(generatedKeys.next());
        } catch (SQLException e) {
            LOGGER.error(DAO_ASSIGN_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_ASSIGN_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_ASSIGN_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_ASSIGN_EXCEPTION.replace("0", ""), e);
        }
    }

    @Test
    public void updateRoleType() throws DaoException, ConnectionManagerException {
        saveRole();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ROLE_QUERY)) {
            int i = 0;
            updateStatement.setString(++i, role.getName().getType());
            updateStatement.setLong(++i, role.getName().getRoleTypeId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Test
    public void deleteRoleType() throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1,role.getName().getRoleTypeId());
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        } catch (SQLException e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Test
    public void getById() throws SQLException, ConnectionManagerException {
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, 0L);
            resultSet = findByIdStatement.executeQuery();
            Assert.assertTrue(resultSet.next());
        } catch (SQLException e) {
            LOGGER.error(EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    @Test
    public void getAll() throws DaoQueryExecuteException, ConnectionManagerException {
        saveRoleRelation(1L);
        saveRole();
        List<Role> roles = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Role> role = buildUser(resultSet);
                role.ifPresent(roles::add);
            }
            Assert.assertEquals(1, roles.size());

        } catch (SQLException | BuildException e) {
            LOGGER.error(EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    private Optional<Role> buildUser(ResultSet resultSet) throws BuildException {
        try {
            EntityBuilder<Role> entityBuilder = new RoleBuilder();
            Role role = entityBuilder.build(resultSet);
            return Optional.of(role);
        } catch (SQLException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new BuildException(BUILD_EXCEPTION.replace("0", ""), e);
        }
    }


    @Test
    public void delete() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {
        saveRoleRelation(1L);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_ROLE_RELATION_QUERY)) {
            deleteStatement.setLong(1, 1L);
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }

    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }
}
