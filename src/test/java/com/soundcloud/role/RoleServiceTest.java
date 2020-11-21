package com.soundcloud.role;

import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DaoException;
import com.soundcloud.service.ServiceException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;

@RunWith(JUnit4.class)
public class RoleServiceTest {
    @Mock
    private RoleService roleService;
    @Mock
    private RoleDao roleDao;

    @BeforeClass
    public static void start() {
        ApplicationContext.initialize();
    }

    @Before
    public void init() {
        ApplicationContext applicationContext = ApplicationContext.getInstance();
        roleDao = Mockito.spy(new RoleDaoImpl(new RoleBuilder(),
                applicationContext.getBean(ConnectionManagerImpl.class)));
        roleService = Mockito.spy(new RoleServiceImpl(roleDao));
    }

    @Test
    public void assignRoleByRole_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(1L).when(roleDao).assignUserRole(new Role());
        roleService.assignRole(new Role());
        Mockito.verify(roleService, Mockito.times(1)).assignRole(new Role());
    }


    @Test
    public void updateRole_isRight() throws ServiceException, DaoException {
        Long userId = 1L;
        Mockito.doReturn(true).when(roleDao).updateUserRole(userId, new Role());
        roleService.updateRole(1L, new Role());
        Mockito.verify(roleService, Mockito.times(1)).updateRole(1L, new Role());
    }


    @Test
    public void deleteRole_isRight() throws ServiceException, DaoException {
        Long userId = 1L;
        Mockito.doReturn(true).when(roleDao).deleteUserRole(userId, 1L);
        roleService.deleteRole(1L, 1L);
        Mockito.verify(roleService, Mockito.times(1)).deleteRole(1L, 1L);
    }

    @Test
    public void getUserRoles_isRight() throws ServiceException, BuildException, DaoException {
        Long userId = 1L;
        Mockito.doReturn(Collections.emptyList()).when(roleDao).getUserRoles(userId);
        roleService.getUserRoles(userId);
        Mockito.verify(roleDao, Mockito.times(1)).getUserRoles(userId);
    }


}
