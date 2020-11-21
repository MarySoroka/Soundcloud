package com.soundcloud.command;

import com.soundcloud.album.*;
import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.artist.*;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DataSource;
import com.soundcloud.dao.DataSourceImpl;
import com.soundcloud.dao.TransactionManager;
import com.soundcloud.dao.TransactionManagerImpl;
import com.soundcloud.role.*;
import com.soundcloud.service.ServiceException;
import com.soundcloud.subscription.*;
import com.soundcloud.track.*;
import com.soundcloud.user.*;
import com.soundcloud.wallet.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class SearchCommandTest {
    @Mock
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    @Mock
    private final UserService userService;
    @Mock
    private final UserDao userDao;


    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
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

    public SearchCommandTest() throws IOException {
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

        userDao = Mockito.spy(new UserDaoImpl(connectionManager, userBuilder));

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
    public void searchCommandExecuteIsRight() throws ServiceException, IOException, ServletException {


        Mockito.doReturn("data").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA);
        Mockito.doReturn(Collections.emptyList()).when(userService).findUsersAlbumsByName(Mockito.anyString());
        Mockito.doNothing().when(request).setAttribute(Mockito.anyString(), Mockito.any());



        Mockito.doReturn(requestDispatcher).when(request).getRequestDispatcher(ApplicationPage.HOME.getPagePath());
        Mockito.doNothing().when(requestDispatcher).forward(request, response);

        Command command = new SearchCommand(userService);
        command.execute(request, response);
        Mockito.verify(userService).findUsersAlbumsByName(Mockito.anyString());
    }
}