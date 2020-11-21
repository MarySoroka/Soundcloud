package com.soundcloud.role;

import com.soundcloud.bean.Bean;
import com.soundcloud.builder.BuildException;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.TransactionSupport;
import com.soundcloud.dao.Transactional;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.soundcloud.application.ApplicationConstants.SERVICE_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.SERVICE_FATAL_EXCEPTION;
/**
 * Class, that implements RoleService and execute business login that relate to album
 */
@Bean(beanName = "RoleService")
@TransactionSupport
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public Long assignRole(Role role) throws ServiceException {
        try {
            roleDao.assignUserRole(role);
            return role.getName().getRoleTypeId();
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Transactional
    @Override
    public Long assignRole(Long userId) throws ServiceException {
        try {
            Role role = new Role(null, null, userId);
            roleDao.assignUserRole(role);
            return role.getName().getRoleTypeId();
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean updateRole(Long roleId, Role role) throws ServiceException {
        try {
            return roleDao.updateUserRole(roleId, role);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public void updateUserRoles(Long userId, List<Role> userRoles) throws ServiceException {
        try {
            Set<Role> roles = getUserRoles(userId);
            Set<Role> rolesTemp = new HashSet<>(roles);
            roles.forEach(role -> userRoles.removeIf(r -> r.equals(role) && rolesTemp.remove(role)));
            if (!rolesTemp.isEmpty()) {
                for (Role role : rolesTemp) {
                    roleDao.deleteUserRole(role.getRoleId(), userId);
                }
            }
            for (Role userRole : userRoles) {
                assignRole(userRole);
            }
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteRole(Long userId, Long roleId) throws ServiceException {
        try {
            return roleDao.deleteUserRole(userId, roleId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public Set<Role> getUserRoles(Long userId) throws ServiceException {
        try {
            return roleDao.getUserRoles(userId);
        } catch (DaoException | BuildException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
