package com.soundcloud.track;

import com.soundcloud.bean.Bean;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.TransactionSupport;
import com.soundcloud.dao.Transactional;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

import static com.soundcloud.application.ApplicationConstants.SERVICE_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.SERVICE_FATAL_EXCEPTION;

@Bean(beanName = "trackServiceImpl")
@TransactionSupport

public class TrackServiceImpl implements TrackService {
    private final TrackDao trackDao;
    private static final Logger LOGGER = LogManager.getLogger(TrackServiceImpl.class);
    public TrackServiceImpl(TrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Override
    @Transactional
    public Long saveTrack(Track track) throws ServiceException {
        try{
            return trackDao.save(track);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean updateTrack(Track track) throws ServiceException {
        try{
            return trackDao.update(track.getId(),track);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteTrack(Long trackId) throws ServiceException {
        try{
            return trackDao.delete(trackId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteAllAlbumsTracks(Long albumId) throws ServiceException {
        try {
            return trackDao.deleteAllAlbumsTracks(albumId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public Set<Track> getAlbumTracks(Long albumId) throws ServiceException {
        try {
            return trackDao.getByAlbumId(albumId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<Track> getTrackByName(String albumName) throws ServiceException {
        try {
            return trackDao.getByName(albumName);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<Track> findTrackByName(String albumName) throws ServiceException {
        try {
            return trackDao.findByName(albumName);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
