package com.soundcloud.role;

import com.soundcloud.album.AlbumBuilder;
import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationContext;
import com.soundcloud.artist.ArtistBuilder;
import com.soundcloud.command.Command;
import com.soundcloud.command.CommandType;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DataSource;
import com.soundcloud.dao.DataSourceImpl;
import com.soundcloud.dao.TransactionManager;
import com.soundcloud.dao.TransactionManagerImpl;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.track.TrackBuilder;
import com.soundcloud.user.User;
import com.soundcloud.user.UserBuilder;
import com.soundcloud.user.UserDto;
import com.soundcloud.wallet.WalletBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class UpdateRolesCommandTest {
    @Mock
    private final RoleService roleService;
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private final SecurityContext securityContext = mock(SecurityContext.class);
    @Mock
    private final HttpSession session = mock(HttpSession.class);

    @Mock
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final User user = new User(
            "flash",
            "123",
            "m@gmail.com",
            0,
            0);


    public UpdateRolesCommandTest() throws IOException {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        UserBuilder userBuilder = new UserBuilder();
        RoleBuilder roleBuilder = new RoleBuilder();
        AlbumBuilder albumBuilder = new AlbumBuilder();
        TrackBuilder trackBuilder = new TrackBuilder();
        WalletBuilder walletBuilder = new WalletBuilder();
        ArtistBuilder artistBuilder = new ArtistBuilder();

        RoleDao roleDao = Mockito.spy(new RoleDaoImpl(roleBuilder, connectionManager));
        roleService = Mockito.spy(new RoleServiceImpl(roleDao));

        ApplicationContext.initialize();
        user.setId(1L);
    }

    @Test
    public void updateRolesCommandExecuteIsRight() throws IOException, ServiceException {
        UserDto userDto = new UserDto(user, 1L);
        SecurityContext.getInstance().authorize(userDto, "");

        Mockito.doReturn(new String[]{RoleType.SINGED_USER.getRoleTypeId().toString()}).when(request).getParameterValues(ApplicationConstants.REQUEST_PARAMETER_USER_ROLE);
        Mockito.doReturn("1").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_USER_ID);
        Mockito.doNothing().when(roleService).updateUserRoles(Mockito.anyLong(), Mockito.anyList());

        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();
        Mockito.doNothing().when(request).setAttribute(
                Mockito.anyString(), Mockito.any());
        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                ""
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ADMIN_USER_PAGE_COMMAND);

        Command command = new UpdateRolesCommand(roleService);
        command.execute(request, response);
        Mockito.verify(roleService).updateUserRoles(Mockito.anyLong(), Mockito.anyList());
        Mockito.verify(request).getContextPath();

    }
}