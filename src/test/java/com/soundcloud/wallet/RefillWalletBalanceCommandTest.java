package com.soundcloud.wallet;

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
import com.soundcloud.role.*;
import com.soundcloud.security.PasswordEncodingException;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.track.TrackBuilder;
import com.soundcloud.user.User;
import com.soundcloud.user.UserBuilder;
import com.soundcloud.user.UserDto;
import com.soundcloud.validation.ValidatorException;
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

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class RefillWalletBalanceCommandTest {
    @Mock
    private final WalletService walletService;
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


    public RefillWalletBalanceCommandTest() throws IOException {
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
         walletService = Mockito.spy(new WalletServiceImpl(walletDao));


        ApplicationContext.initialize();
        user.setId(1L);
    }

    @Test
    public void refillWalletBalanceCommandExecuteIsRight() throws IOException, PasswordEncodingException, ValidatorException, ServletException, ServiceException {


        UserDto userDto = new UserDto(user,1L);
        SecurityContext.getInstance().authorize(userDto,"");

        Mockito.doReturn("10").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_WALLET_AMOUNT);
        Mockito.doReturn("1").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_WALLET_ID);
        Mockito.doReturn(true).when(walletService).setWalletAmount(Mockito.anyLong(), Mockito.any(Wallet.class));

        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();
        Mockito.doNothing().when(request).setAttribute(
                Mockito.anyString(), Mockito.any());
        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                ""
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());

        Command command = new RefillWalletBalanceCommand(walletService);
        command.execute(request, response);
        Mockito.verify(walletService).setWalletAmount(Mockito.anyLong(),Mockito.any(Wallet.class));
        Mockito.verify(request).getContextPath();

    }

}