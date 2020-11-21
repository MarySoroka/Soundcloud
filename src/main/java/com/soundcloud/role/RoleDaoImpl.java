package com.soundcloud.role;

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
import java.util.*;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "RoleDao")
public class RoleDaoImpl implements RoleDao {


    private static final Logger LOGGER = LogManager.getLogger(RoleDaoImpl.class);

    private static final String SELECT_ALL_QUERY = "select role_id,role_name, user_id from soundcloud.user_role join soundcloud.user_role_has_user on role_id = role_id";
    private static final String SELECT_BY_ID_QUERY = "select role_id,role_name, user_id from soundcloud.user_role join soundcloud.user_role_has_user on role_id = role_id where role_id = ? ";
    private static final String INSERT_ROLE_QUERY = "INSERT INTO soundcloud.user_role (role_name) VALUES(?);";
    private static final String INSERT_ROLE_RELATION_QUERY = "INSERT INTO soundcloud.user_role_has_user (user_id, role_id) VALUES(?,?);";
    private static final String UPDATE_ROLE_QUERY = "update soundcloud.user_role set role_name=?  where role_id = ?";
    private static final String UPDATE_ROLE_RELATION_QUERY = "update soundcloud.user_role_has_user set user_id=?  where role_id = ?";
    private static final String DELETE_ROLE_QUERY = "delete from soundcloud.user_role where role_id = ?";
    private static final String DELETE_ROLE_RELATION_QUERY = "delete from soundcloud.user_role_has_user where role_id = ? and user_id=?";
    private static final String SELECT_BY_USER_ID_QUERY = "select r.role_id,u.role_name, r.user_id from soundcloud.user_role u join soundcloud.user_role_has_user r on u.role_id = r.role_id where r.user_id = ? ";

    private final EntityBuilder<Role> roleBuilder;
    private final ConnectionManager connectionManager;

    public RoleDaoImpl(@BeanQualifier(value = "RoleBuilder") EntityBuilder<Role> roleEntityBuilder, ConnectionManager connectionManager) {
        this.roleBuilder = roleEntityBuilder;
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean delete(Long key) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1, key);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        }    }



    @Override
    public boolean update(Long key, Role role) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ROLE_QUERY)) {
            int i = 0;
            updateStatement.setString(++i, role.getName().getType() );
            updateStatement.setLong(++i, key);
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        }

    }

    @Override
    public Long save(Role role) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement assignStatement = connection.prepareStatement(INSERT_ROLE_QUERY,Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            assignStatement.setLong(++i, role.getName().getRoleTypeId());
            assignStatement.setString(++i, role.getName().getType());
            assignStatement.executeUpdate();
            ResultSet generatedKeys = assignStatement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(DAO_ASSIGN_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_ASSIGN_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_ASSIGN_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_ASSIGN_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public Optional<Role> getById(Long roleId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, roleId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            return build(resultSet);
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public List<Role> getAll() throws DaoException{
        List<Role> roles = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Role> role = build(resultSet);
                role.ifPresent(roles::add);
            }
            return roles;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }


    @Override
    public Role clone(Role role) {
        return new Role(role);
    }

    @Override
    public Optional<Role> build(ResultSet data) throws BuildException {
        Role role = roleBuilder.build(data);
        return Optional.of(role);
    }

    @Override
    public void assignUserRole(Role role) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_ROLE_RELATION_QUERY)) {
            int i = 0;
            if (role.getName() == null) {
                role.setName(RoleType.UNSIGNED_USER);
            }
            saveStatement.setLong(++i, role.getUserId());
            saveStatement.setLong(++i, role.getName().getRoleTypeId());
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
    public boolean updateUserRole(Long roleId, Role role) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ROLE_RELATION_QUERY)) {
            int i = 0;
            updateStatement.setLong(++i, roleId);
            updateStatement.setLong(++i, role.getUserId());
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public boolean deleteUserRole(Long roleId, Long userId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_ROLE_RELATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            deleteStatement.setLong(++i, roleId);
            deleteStatement.setLong(++i, userId);
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
    public Set<Role> getUserRoles(Long userId) throws DaoException, BuildException {
        Set<Role> roles = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, userId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Role> role = build(resultSet);
                role.ifPresent(roles::add);
            }
            return roles;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new BuildException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }
}
