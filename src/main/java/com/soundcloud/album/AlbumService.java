package com.soundcloud.album;

import com.soundcloud.artist.Artist;
import com.soundcloud.service.ServiceException;
import com.soundcloud.track.Track;

import java.util.List;

public interface AlbumService {
    Long uploadAlbum(Album album, List<Track> tracks, Long userId) throws ServiceException;
    boolean editAlbum(Long key, Album entity, List<Track> tracks) throws ServiceException;
    boolean deleteAlbum(Long albumId) throws ServiceException;
    boolean saveLikedAlbum(Long albumId, Long userId, Integer likesAmount) throws ServiceException;
    boolean deleteLikedAlbum(Long albumId, Long userId, Integer likesAmount) throws ServiceException;
    List<Album> getArtistAlbums(Long userId) throws ServiceException;
    List<Album> getLikedAlbums(Long userId) throws ServiceException;
    List<Album> getAlbumByName(String albumName) throws ServiceException;
    List<Album> findAlbumByName(String albumName) throws ServiceException;
    Artist getArtistByAlbum(Long artistId) throws ServiceException;
    Album getAlbumById(Long albumId) throws ServiceException;
    boolean isLiked(Long userId, Long albumId) throws ServiceException;
    String getArtistAlbumName(Long albumId) throws ServiceException;
    List<Album> getAll() throws ServiceException;
    boolean updateAlbum(Long albumId, Album album) throws ServiceException;
    boolean updateAlbumWithIcon(Long albumId, Album album) throws ServiceException;
}
