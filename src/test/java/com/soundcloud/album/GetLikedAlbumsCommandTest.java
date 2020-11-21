package com.soundcloud.album;

import com.soundcloud.application.ApplicationPage;
import com.soundcloud.artist.*;
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
import com.soundcloud.track.*;
import com.soundcloud.user.User;
import com.soundcloud.user.UserDto;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.LinkedList;

import static com.soundcloud.application.ApplicationConstants.*;
import static org.mockito.Mockito.mock;

public class GetLikedAlbumsCommandTest {
    @Mock
    private final InputStream resourceAsStream = mock(InputStream.class);
    @Mock
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    @Mock
    private final AlbumService albumService;
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private final HttpSession session = mock(HttpSession.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final User user = new User(
            "flash",
            "123",
            "m@gmail.com",
            0,
            0);
    private final Album album;
    @Mock
    private final Part part = mock(Part.class);

    public GetLikedAlbumsCommandTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        AlbumBuilder albumBuilder = new AlbumBuilder();
        TrackBuilder trackBuilder = new TrackBuilder();
        ArtistBuilder artistBuilder = new ArtistBuilder();

        AlbumDao albumDao = Mockito.spy(new AlbumDaoImpl(connectionManager, albumBuilder));

        ArtistDao artistDao = Mockito.spy(new ArtistDaoImpl(connectionManager, artistBuilder));
        ArtistService artistService = Mockito.spy(new ArtistServiceImpl(artistDao));

        TrackDao trackDao = Mockito.spy(new TrackDaoImpl(connectionManager, trackBuilder));
        TrackService trackService = Mockito.spy(new TrackServiceImpl(trackDao));

        albumService = Mockito.spy(new AlbumServiceImpl(albumDao, trackService, artistService));

        album = new Album("la", LocalDate.now(ZoneId.of("America/Montreal")), 0, AlbumState.ALBUM, AlbumGenre.COUNTRY);
        album.setArtistId(1L);
        album.setArtistName("");
        album.setLikesAmount(1);
    }

    @Test
    public void getLikedAlbumCommandExecuteIsRight() throws IOException, ServiceException {
        user.setArtistId(1L);
        user.setId(1L);
        album.setId(1L);
        SecurityContext.getInstance().authorize(new UserDto(user, 1L), "");

        LinkedList<Album> albums = new LinkedList<>(Collections.singleton(album));
        Mockito.doReturn(albums).when(albumService).getLikedAlbums(Mockito.anyLong());

        Mockito.doReturn(ApplicationPage.ALBUM.getPagePath()).when(request).getParameter(REQUEST_PARAMETER_PAGE);
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();

        Mockito.doNothing().when(request).setAttribute(REQUEST_PARAMETER_ALBUM_LIKED,albums);


        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                ""  + "?" + COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_PAGE_COMMAND + "&" +
                        REQUEST_PARAMETER_PAGE + "=" +ApplicationPage.ALBUM.getPagePath());

        Command command = new GetLikedAlbumsCommand(albumService);
        command.execute(request, response);

        Mockito.verify(request).setAttribute(REQUEST_PARAMETER_ALBUM_LIKED, albums);


    }
}