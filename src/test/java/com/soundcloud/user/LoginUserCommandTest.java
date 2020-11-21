package com.soundcloud.user;

import com.soundcloud.album.*;
import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.artist.*;
import com.soundcloud.command.Command;
import com.soundcloud.command.CommandType;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DataSource;
import com.soundcloud.dao.DataSourceImpl;
import com.soundcloud.dao.TransactionManager;
import com.soundcloud.dao.TransactionManagerImpl;
import com.soundcloud.role.*;
import com.soundcloud.security.PasswordEncodingException;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.security.SecurityUtil;
import com.soundcloud.service.ServiceException;
import com.soundcloud.subscription.*;
import com.soundcloud.track.*;
import com.soundcloud.wallet.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class LoginUserCommandTest {
    @Mock
    private final UserService userService;
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private final SecurityContext securityContext = mock(SecurityContext.class);
    @Mock
    private final HttpSession session = mock(HttpSession.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final UserDto dto = new UserDto(new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L), 1L);

    public LoginUserCommandTest() throws IOException {
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
        RoleService roleService = Mockito.spy(new RoleServiceImpl(roleDao));

        WalletDao walletDao = Mockito.spy(new WalletDaoImpl(walletBuilder, connectionManager));
        WalletService walletService = Mockito.spy(new WalletServiceImpl(walletDao));

        UserDao userDao = Mockito.spy(new UserDaoImpl(connectionManager, userBuilder));

        AlbumDao albumDao = Mockito.spy(new AlbumDaoImpl(connectionManager, albumBuilder));

        ArtistDao artistDao = Mockito.spy(new ArtistDaoImpl(connectionManager, artistBuilder));
        ArtistService artistService = Mockito.spy(new ArtistServiceImpl(artistDao));

        TrackDao trackDao = Mockito.spy(new TrackDaoImpl(connectionManager, trackBuilder));
        TrackService trackService = Mockito.spy(new TrackServiceImpl(trackDao));

        AlbumService albumService = Mockito.spy(new AlbumServiceImpl(albumDao, trackService, artistService));
        SubscriptionBuilder subscriptionBuilder = new SubscriptionBuilder();

        SubscriptionDao subscriptionDao = Mockito.spy(new SubscriptionDaoImpl(connectionManager, subscriptionBuilder));
        SubscriptionService subscriptionService = Mockito.spy(new SubscriptionServiceImpl(subscriptionDao));

        userService = Mockito.spy(new UserServiceImpl(roleService, walletService, userDao, albumService, artistService, trackService,subscriptionService));

    }

    @Test
    public void loginCommandExecuteIsRight() throws ServiceException, IOException, PasswordEncodingException {
        Mockito.doReturn(dto.getLogin()).when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_LOGIN);
        Mockito.doReturn(dto.getPassword()).when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_PASSWORD);
        Optional<UserDto> optionalUserDto = Optional.of(this.dto);
        Mockito.doReturn(optionalUserDto).when(userService).loginUser(this.dto.getLogin(), SecurityUtil.encode(this.dto.getPassword()));
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();
        Mockito.doNothing().when(securityContext).authorize(this.dto, "");
        Mockito.doNothing().when(request).setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, false);
        Mockito.doNothing().when(request).setAttribute(ApplicationConstants.REQUEST_ARTIST_ID, 1L);
        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                ""
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND +
                        "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + 1L);
        Command command = new LoginUserCommand(userService);
        command.execute(request,response);
        Assert.assertNotNull(SecurityContext.getInstance().getCurrentUser());
    }
}