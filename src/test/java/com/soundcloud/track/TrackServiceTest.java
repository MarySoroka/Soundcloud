package com.soundcloud.track;

import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.service.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import static com.soundcloud.application.ApplicationConstants.DATABASE_PROPERTY_FILE;

public class TrackServiceTest {
    public TrackServiceTest() {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        TrackBuilder trackBuilder = new TrackBuilder();
        trackDao = Mockito.spy(new TrackDaoImpl(connectionManager, trackBuilder));
        this.trackService = Mockito.spy(new TrackServiceImpl(trackDao));
        track = new Track(0L, "name", 0, 1L, "");

    }

    private final Track track;
    @Mock
    private final TrackDao trackDao;
    @Mock
    private final TrackService trackService;

    @Test
    public void saveTrack_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(track.getId()).when(trackDao).save(Mockito.any(Track.class));
        Assert.assertTrue(trackService.saveTrack(track)>=0);

    }

    @Test
    public void updateTrack_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(trackDao).update(Mockito.anyLong(),Mockito.any(Track.class));
        Assert.assertTrue(trackService.updateTrack(track));
    }

    @Test
    public void deleteTrack_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(trackDao).delete(Mockito.anyLong());
        Assert.assertTrue(trackService.deleteTrack(track.getId()));
    }

    @Test
    public void deleteAllAlbumsTracks_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(trackDao).deleteAllAlbumsTracks(Mockito.anyLong());
        Assert.assertTrue(trackService.deleteAllAlbumsTracks(track.getAlbumId()));

    }

    @Test
    public void getAlbumTracks_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new HashSet<>(Collections.singleton(track))).when(trackDao).getByAlbumId(Mockito.anyLong());
        Assert.assertFalse(trackService.getAlbumTracks(track.getAlbumId()).isEmpty());
    }

    @Test
    public void getTrackByName_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(track))).when(trackDao).getByName(Mockito.anyString());
        Assert.assertFalse(trackService.getTrackByName(track.getName()).isEmpty());

    }
}
