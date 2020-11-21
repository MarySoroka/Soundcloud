package com.soundcloud.artist;

import com.soundcloud.bean.Bean;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.TransactionSupport;
import com.soundcloud.dao.Transactional;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.SERVICE_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.SERVICE_FATAL_EXCEPTION;
/**
 * Class, that implements ArtistService and execute business login that relate to album
 */
@Bean(beanName = "artistServiceImpl")
@TransactionSupport

public class ArtistServiceImpl implements ArtistService{
    private final ArtistDao artistDao;
    private static final Logger LOGGER = LogManager.getLogger(ArtistServiceImpl.class);

    public ArtistServiceImpl(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public Artist getUserByArtistId(Long artistId) throws ServiceException {
        try {
            Optional<Artist> artist = artistDao.getById(artistId);
            if (artist.isPresent()){
                return artist.get();
            }
            throw new ServiceException(SERVICE_EXCEPTION.replace("0",""));
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public Long getArtistIdByUserId(Long userId) throws ServiceException {
        try {
            return artistDao.getArtistIdByUserId(userId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public String getArtistName(Long artistId) throws ServiceException {
        try {
            return artistDao.getArtistName(artistId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    @Transactional
    public Long saveArtist(Artist artist) throws ServiceException {
        try {
            return artistDao.save(artist);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteArtist(Long artistId) throws ServiceException {
        try {
            return artistDao.delete(artistId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean updateArtist(Artist artist) throws ServiceException {
        try {
            return artistDao.update(artist.getArtistId(),artist);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
