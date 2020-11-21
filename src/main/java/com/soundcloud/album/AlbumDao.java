package com.soundcloud.album;

import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.EntityDao;

import java.util.List;

/**
 * Album dao interface, that contains method for database interaction
 */
public interface AlbumDao extends EntityDao<Album,Long> {
    boolean saveLikedAlbum(Long albumId, Long userId) throws DaoException;
    boolean isLiked(Long userId, Long albumId) throws DaoException;
    boolean deleteLikedAlbum(Long albumId, Long userId) throws DaoException;
    List<Album> getArtistAlbum(Long userId) throws DaoException;
    List<Album> getLikedAlbums(Long userId) throws DaoException;
    boolean updateLikesAmount(Long albumId, Integer likesAmount) throws DaoException;
    boolean updateWithIcon(Long albumId,Album album ) throws DaoException;
    List<Album> getAlbumByName(String albumName) throws DaoException;
    List<Album> findAlbumByName(String albumName) throws DaoException;
}
