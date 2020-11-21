package com.soundcloud.track;

import com.soundcloud.album.AlbumBuilder;
import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.artist.ArtistBuilder;
import com.soundcloud.artist.ArtistDao;
import com.soundcloud.artist.ArtistDaoImpl;
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
import com.soundcloud.user.User;
import com.soundcloud.user.UserDto;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

public class UploadTrackCommandTest {
    @Mock
    private final TrackService trackService;
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private final Part part = mock(Part.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final User user = new User(
            "flash",
            "123",
            "m@gmail.com",
            0,
            0);


    public UploadTrackCommandTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        AlbumBuilder albumBuilder = new AlbumBuilder();
        TrackBuilder trackBuilder = new TrackBuilder();
        ArtistBuilder artistBuilder = new ArtistBuilder();
        ArtistDao artistDao = Mockito.spy(new ArtistDaoImpl(connectionManager, artistBuilder));
        TrackDao trackDao = Mockito.spy(new TrackDaoImpl(connectionManager, trackBuilder));
        trackService = Mockito.spy(new TrackServiceImpl(trackDao));

    }

    @Test
    public void uploadTrackCommandExecuteIsRight() throws IOException, ServiceException, ServletException {

        user.setArtistId(1L);
        SecurityContext.getInstance().authorize(new UserDto(user, 1L), "");
        Mockito.doReturn("1").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID);

        Mockito.doReturn(0L).when(trackService).saveTrack(Mockito.any());

        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                "" + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ALBUM_COMMAND + "&albumId=" + 1);

        Command command = new UploadTrackCommand(trackService);
        command.execute(request, response);

    }
}