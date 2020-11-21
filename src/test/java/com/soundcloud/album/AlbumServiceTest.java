package com.soundcloud.album;

import com.soundcloud.artist.*;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.service.ServiceException;
import com.soundcloud.track.*;
import com.soundcloud.user.User;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;

public class AlbumServiceTest {
    public AlbumServiceTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        AlbumBuilder albumBuilder = new AlbumBuilder();
        TrackBuilder trackBuilder = new TrackBuilder();
        ArtistBuilder artistBuilder = new ArtistBuilder();

        AlbumDao albumDao = Mockito.spy(new AlbumDaoImpl(connectionManager, albumBuilder));

        ArtistDao artistDao = Mockito.spy(new ArtistDaoImpl(connectionManager, artistBuilder));
        ArtistService artistService = Mockito.spy(new ArtistServiceImpl(artistDao));
        this.artistService = artistService;

        TrackDao trackDao = Mockito.spy(new TrackDaoImpl(connectionManager, trackBuilder));
        TrackService trackService = Mockito.spy(new TrackServiceImpl(trackDao));
        this.trackService = trackService;

        this.albumDao = albumDao;
        this.albumService = Mockito.spy(new AlbumServiceImpl(albumDao, trackService, artistService));

        album = new Album("la", LocalDate.now(ZoneId.of("America/Montreal")), 0, AlbumState.ALBUM, AlbumGenre.COUNTRY);
        album.setArtistId(1L);
        album.setArtistName("");
        album.setLikesAmount(1);
    }

    private final Album album;
    @Mock
    private final AlbumDao albumDao;
    @Mock
    private final AlbumService albumService;
    @Mock
    private final ArtistService artistService;
    @Mock
    private final TrackService trackService;


    private final User user = new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L);


    @Test
    public void uploadAlbum_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(1L);
        Mockito.doReturn(1L).when(albumDao).save(Mockito.any(Album.class));
        Mockito.doReturn(1L).when(trackService).saveTrack(Mockito.any(Track.class));
        Assert.assertEquals(new Long(1L), albumService.uploadAlbum(album, new LinkedList<>(), 1L));

    }

    @Test
    public void editAlbum_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(trackService).updateTrack(Mockito.any(Track.class));
        Mockito.doReturn(true).when(albumDao).update(1L, album);
        Assert.assertTrue(albumService.editAlbum(1L, album, new LinkedList<>()));
    }

    @Test
    public void deleteAlbum_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(albumDao).delete(1L);
        Assert.assertTrue(albumService.deleteAlbum(1L));

    }

    @Test
    public void saveLikedAlbum_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(albumDao).updateLikesAmount(Mockito.anyLong(), Mockito.anyInt());
        Mockito.doReturn(true).when(albumDao).saveLikedAlbum(Mockito.anyLong(), Mockito.anyLong());
        Assert.assertTrue(albumService.saveLikedAlbum(1L, 1L, album.getLikesAmount()));
    }

    @Test
    public void deleteLikedAlbum_isRight() throws DaoException, ServiceException {
        Mockito.doReturn(true).when(albumDao).updateLikesAmount(Mockito.anyLong(), Mockito.anyInt());
        Mockito.doReturn(true).when(albumDao).deleteLikedAlbum(Mockito.anyLong(), Mockito.anyLong());
        Assert.assertTrue(albumService.deleteLikedAlbum(1L, 1L, album.getLikesAmount()));

    }


    @Test
    public void getArtistAlbums_isRight() throws ServiceException, DaoException {
        album.setId(1L);
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumDao).getArtistAlbum(Mockito.anyLong());
        Mockito.doReturn(new HashSet<>()).when(trackService).getAlbumTracks(Mockito.anyLong());
        Assert.assertEquals(1, albumService.getArtistAlbums(1L).size());
    }

    @Test
    public void getLikedAlbums_isRight() throws ServiceException, DaoException {
        album.setId(1L);
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumDao).getLikedAlbums(Mockito.anyLong());
        Mockito.doReturn(new HashSet<>()).when(trackService).getAlbumTracks(Mockito.anyLong());
        Assert.assertEquals(1, albumService.getLikedAlbums(1L).size());
    }

    @Test
    public void getAlbumByName_isRight() throws DaoException, ServiceException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumDao).getAlbumByName(Mockito.anyString());
        Assert.assertEquals(1, albumService.getAlbumByName("").size());
    }

    @Test
    public void getArtistByAlbum_isRight() throws ServiceException {
        Artist artist = new Artist(1L, 1L);
        Mockito.doReturn(artist).when(artistService).getUserByArtistId(Mockito.anyLong());
        Assert.assertEquals(artist, albumService.getArtistByAlbum(1L));
    }

    @Test
    public void getAlbumById_isRight() throws ServiceException, DaoException {
        album.setId(1L);
        Mockito.doReturn(Optional.of(album)).when(albumDao).getById(Mockito.anyLong());
        Mockito.doReturn(new HashSet<>()).when(trackService).getAlbumTracks(Mockito.anyLong());
        Assert.assertNotNull(albumService.getAlbumById(1L));
    }

    @Test
    public void isLiked_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(albumDao).isLiked(1L, 1L);
        Assert.assertTrue(albumService.isLiked(1L, 1L));
    }

    @Test
    public void getArtistAlbumName_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(Optional.of(album)).when(albumDao).getById(Mockito.anyLong());
        Mockito.doReturn("").when(artistService).getArtistName(Mockito.anyLong());
        Assert.assertEquals("", albumService.getArtistAlbumName(1L));
    }

    @Test
    public void getAll_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumDao).getAll();
        Assert.assertFalse(albumService.getAll().isEmpty());

    }

    @Test
    public void updateAlbum_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(albumDao).update(1L, album);
        Assert.assertTrue(albumService.updateAlbum(1L, album));

    }

    @Test
    public void updateAlbumWithIcon_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(albumDao).updateWithIcon(1L, album);
        Assert.assertTrue(albumService.updateAlbumWithIcon(1L, album));

    }

}
