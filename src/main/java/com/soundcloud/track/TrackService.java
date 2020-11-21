package com.soundcloud.track;

import com.soundcloud.service.ServiceException;

import java.util.List;
import java.util.Set;

/**
 * Track service interface, that contains method for business logic realisation
 */
public interface TrackService {
    /**
     * method save track
     *
     * @param track track
     * @return track id
     * @throws ServiceException if we get exception from dao
     */
    Long saveTrack(Track track) throws ServiceException;

    /**
     * method update track
     *
     * @param track track
     * @return if track has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean updateTrack(Track track) throws ServiceException;

    /**
     * method delete track
     *
     * @param trackId track id
     * @return if track has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteTrack(Long trackId) throws ServiceException;

    /**
     * method delete tracks of the album
     *
     * @param albumId album id
     * @return if track has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteAllAlbumsTracks(Long albumId) throws ServiceException;

    /**
     * method get all album tracks
     *
     * @param albumId album id
     * @return album tracks
     * @throws ServiceException if we get exception from dao
     */
    Set<Track> getAlbumTracks(Long albumId) throws ServiceException;

    /**
     * method get track by name
     *
     * @param albumName album name
     * @return list of tracks
     * @throws ServiceException if we get exception from dao
     */
    List<Track> getTrackByName(String albumName) throws ServiceException;
    /**
     * method find all tracks
     *
     * @param albumName album name
     * @return list of tracks
     * @throws ServiceException if we get exception from dao
     */
    List<Track> findTrackByName(String albumName) throws ServiceException;
}
