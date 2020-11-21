package com.soundcloud.role;

import com.soundcloud.service.ServiceException;

import java.util.List;
import java.util.Set;

/**
 * Role service interface, that contains method for business logic realisation
 */
public interface RoleService {
    /**
     * method assign role
     *
     * @param role role
     * @return role id, that has been generated
     * @throws ServiceException if we get exception from dao
     */
    Long assignRole(Role role) throws ServiceException;

    /**
     * method assign role
     *
     * @param userId user id
     * @return role id, that has been generated
     * @throws ServiceException if we get exception from dao
     */
    Long assignRole(Long userId) throws ServiceException;

    /**
     * method update user roles
     *
     * @param userId    userId
     * @param userRoles user roles
     * @throws ServiceException if we get exception from dao
     */
    void updateUserRoles(Long userId, List<Role> userRoles) throws ServiceException;

    /**
     * method update user roles
     *
     * @param roleId role id
     * @param role   role
     * @return if role has been updated, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean updateRole(Long roleId, Role role) throws ServiceException;

    /**
     * method delete user roles
     *
     * @param roleId role id
     * @param userId user id
     * @return if role has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteRole(Long userId, Long roleId) throws ServiceException;

    /**
     * method return all user roles
     *
     * @param userId user id
     * @return set of user's roles
     * @throws ServiceException if we get exception from dao
     */
    Set<Role> getUserRoles(Long userId) throws ServiceException;
}
