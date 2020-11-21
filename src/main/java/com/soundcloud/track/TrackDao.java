package com.soundcloud.track;

import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.EntityDao;

import java.util.List;
import java.util.Set;

/**
 * Subscription dao interface, that contains method for database interaction
 */
public interface TrackDao extends EntityDao<Track, Long> {
    /**
     * method return track set of the album
     *
     * @param albumId album id
     * @return track set
     * @throws DaoException if we get exception in sql request
     */
    Set<Track> getByAlbumId(Long albumId) throws DaoException;

    /**
     * method find tracks by name
     *
     * @param trackName track name
     * @return list of tracks
     * @throws DaoException if we get exception in sql request
     */
    List<Track> getByName(String trackName) throws DaoException;

    /**
     * method find tracks by name
     *
     * @param trackName track name
     * @return list of tracks
     * @throws DaoException if we get exception in sql request
     */
    List<Track> findByName(String trackName) throws DaoException;

    /**
     * method delete subscription
     *
     * @param albumId album id
     * @return if subscription has been deleted, then return true, else false
     * @throws DaoException if we get exception in sql request
     */
    boolean deleteAllAlbumsTracks(Long albumId) throws DaoException;

}
