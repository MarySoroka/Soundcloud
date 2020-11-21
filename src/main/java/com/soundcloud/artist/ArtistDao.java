package com.soundcloud.artist;

import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.EntityDao;

/**
 * Artist dao interface, that contains method for database interaction
 */
public interface ArtistDao extends EntityDao<Artist, Long> {
    /**
     * method return  artist id by user id
     *
     * @param userId user id
     * @return artist id
     * @throws DaoException if we get exception in sql request
     */
    Long getArtistIdByUserId(Long userId) throws DaoException;
    /**
     * method return  artist name by artist id
     *
     * @param artistId artist id
     * @return artist name
     * @throws DaoException if we get exception in sql request
     */
    String getArtistName(Long artistId) throws DaoException;
}
