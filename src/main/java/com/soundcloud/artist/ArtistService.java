package com.soundcloud.artist;

import com.soundcloud.service.ServiceException;

/**
 * Artist service interface, that contains method for business logic realisation
 */
public interface ArtistService {
    /**
     * method return userId by artist id
     *
     * @param artistId artist id
     * @return artist object
     * @throws ServiceException if we get exception from dao
     */
    Artist getUserByArtistId(Long artistId) throws ServiceException;

    /**
     * method return artist id by user id
     *
     * @param userId user id
     * @return artist id
     * @throws ServiceException if we get exception from dao
     */
    Long getArtistIdByUserId(Long userId) throws ServiceException;

    /**
     * method return artist name by artist id
     *
     * @param artistId artist id
     * @return artist name
     * @throws ServiceException if we get exception from dao
     */
    String getArtistName(Long artistId) throws ServiceException;

    /**
     * method save artist
     *
     * @param artist artist object
     * @return artist id
     * @throws ServiceException if we get exception from dao
     */
    Long saveArtist(Artist artist) throws ServiceException;

    /**
     * method delete artist by artist id
     *
     * @param artistId artist id
     * @return if artist has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteArtist(Long artistId) throws ServiceException;

    /**
     * method update artist by artist id
     *
     * @param artist artist object
     * @return if artist has been updated, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean updateArtist(Artist artist) throws ServiceException;

}
