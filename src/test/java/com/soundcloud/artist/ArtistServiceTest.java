package com.soundcloud.artist;

import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.service.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;

@RunWith(JUnit4.class)
public class ArtistServiceTest {
    public ArtistServiceTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        ArtistBuilder artistBuilder = new ArtistBuilder();

        artistDao = Mockito.spy(new ArtistDaoImpl(connectionManager, artistBuilder));
        this.artistService = Mockito.spy(new ArtistServiceImpl(artistDao));

        artist = new Artist(1L,1L);
    }

    private final Artist artist;
    @Mock
    private final ArtistDao artistDao;
    @Mock
    private final ArtistService artistService;


    @Test
    public void getUserByArtistId_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(Optional.of(artist)).when(artistDao).getById(Mockito.anyLong());
        Assert.assertEquals(artist.getUserId(),artistService.getUserByArtistId(artist.getArtistId()).getUserId());

    }

    @Test
    public void getArtistIdByUserId_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(artist.getArtistId()).when(artistDao).getArtistIdByUserId(Mockito.anyLong());
        Assert.assertEquals(artist.getArtistId(),artistService.getArtistIdByUserId(artist.getUserId()));
    }

    @Test
    public void getArtistName_isRight() throws ServiceException, DaoException {
        Mockito.doReturn("").when(artistDao).getArtistName(Mockito.anyLong());
        Assert.assertEquals("",artistService.getArtistName(artist.getUserId()));
    }

    @Test
    public void saveArtist_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(1L).when(artistDao).save(Mockito.any(Artist.class));
        Assert.assertTrue(artistService.saveArtist(artist)>0);

    }

    @Test
    public void deleteArtist_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(artistDao).delete(Mockito.anyLong());
        Assert.assertTrue(artistService.deleteArtist(artist.getUserId()));
    }

    @Test
    public void updateArtist_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(artistDao).update(Mockito.anyLong(),Mockito.any(Artist.class));
        Assert.assertTrue(artistService.updateArtist(artist));

    }
}
