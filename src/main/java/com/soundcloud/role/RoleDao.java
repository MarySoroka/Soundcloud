package com.soundcloud.role;

import com.soundcloud.dao.EntityDao;
import com.soundcloud.builder.BuildException;
import com.soundcloud.dao.DaoException;

import java.util.Set;

/**
 * Role dao interface, that contains method for database interaction
 */
public interface RoleDao extends EntityDao<Role, Long> {
    /**
     * method assign new role for user
     *
     * @param role role
     * @throws DaoException if we get exception in sql request
     */
    void assignUserRole(Role role) throws DaoException;

    /**
     * method update user roles
     *
     * @param roleId role id
     * @param role   role
     * @return if role has been updated, then return true, else false
     * @throws DaoException if we get exception in sql request
     */
    boolean updateUserRole(Long roleId, Role role) throws DaoException;

    /**
     * method delete user roles
     *
     * @param roleId role id
     * @param userId user id
     * @return if role has been deleted, then return true, else false
     * @throws DaoException if we get exception in sql request
     */
    boolean deleteUserRole(Long roleId, Long userId) throws DaoException;

    /**
     * method return all user roles
     *
     * @param userId user id
     * @return set of user's roles
     * @throws DaoException   if we get exception in sql request
     * @throws BuildException if we get exception in building
     */
    Set<Role> getUserRoles(Long userId) throws DaoException, BuildException;
}
