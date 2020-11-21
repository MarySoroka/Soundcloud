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
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.subscription.*;
import com.soundcloud.track.*;
import com.soundcloud.wallet.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

public class EditProfileInformationCommandTest {
    @Mock
    private final UserService userService;
    @Mock
    private final Part part = mock(Part.class);
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private final SecurityContext securityContext = mock(SecurityContext.class);
    @Mock
    private final HttpSession session = mock(HttpSession.class);
    private final UserDto dto = new UserDto(new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L), 1L);

    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final User user = new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L);

    public EditProfileInformationCommandTest() throws IOException {
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
    public void editProfileCommandExecuteIsRight() throws ServiceException, IOException, ServletException {
        user.setArtistId(1L);
        SecurityContext.getInstance().authorize(new UserDto(user, 1L), "");

        Mockito.doReturn(user.getLogin()).when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_LOGIN);
        Mockito.doReturn(user.getPassword()).when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_PASSWORD);
        Mockito.doReturn(part).when(request).getPart(ApplicationConstants.REQUEST_PARAMETER_IMAGE);
        Mockito.doReturn(this.getClass().getResourceAsStream("/avatar.png")).when(part).getInputStream();
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();
        Mockito.doReturn(true).when(userService).updateUserInformation(Mockito.any(User.class));

        Mockito.doReturn(user).when(userService).getUser(Mockito.anyLong());
        Mockito.doReturn(dto).when(userService).getByIdUser(Mockito.anyLong());

        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                ""
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());

        Command command = new EditProfileInformationCommand(userService);
        command.execute(request, response);
        Mockito.verify(userService).updateUserInformation(Mockito.any(User.class));
    }
}
