package com.soundcloud.album;

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
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.track.*;
import com.soundcloud.user.User;
import com.soundcloud.user.UserDto;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;
import static org.mockito.Mockito.mock;

public class DeleteLikeAlbumCommandTest  {
    @Mock
    private final AlbumService albumService;
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final User user = new User(
            "flash",
            "123",
            "m@gmail.com",
            0,
            0);


    public DeleteLikeAlbumCommandTest() {
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
    }

    @Test
    public void deleteLikedAlbumCommandExecuteIsRight() throws IOException, ServiceException {


        user.setArtistId(1L);
        user.setId(1L);
        SecurityContext.getInstance().authorize(new UserDto(user, 1L), "");
        Mockito.doReturn("1").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID);
        Mockito.doReturn("1").when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_LIKES_AMOUNT);


        Mockito.doReturn(true).when(albumService).deleteLikedAlbum(Mockito.anyLong(),Mockito.anyLong(),Mockito.anyInt());

        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                "" +"?"+ApplicationConstants.COMMAND_NAME_PARAM+"="+CommandType.GET_ALBUM_COMMAND + "&albumId=" + 1);

        Command command = new DeleteLikeAlbumCommand(albumService);
        command.execute(request, response);
        Mockito.verify(albumService).deleteLikedAlbum(Mockito.any(),Mockito.anyLong(),Mockito.anyInt());

    }
}