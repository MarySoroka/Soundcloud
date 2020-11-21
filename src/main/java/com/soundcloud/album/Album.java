package com.soundcloud.album;

import com.soundcloud.track.Track;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Class of user album
 */
public class Album {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private Integer likesAmount;
    private AlbumState albumState;
    private InputStream albumIcon;
    private Set<Track> trackList;
    private AlbumGenre albumGenre;
    private String artistName;


    public Album(Long albumId, String albumName, AlbumState albumState, AlbumGenre albumGenre) {
        this.id = albumId;
        this.name = albumName;
        this.albumState = albumState;
        this.albumGenre = albumGenre;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    private Long artistId;

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    private String albumIconBase64;

    public String getAlbumIconBase64() {
        return albumIconBase64;
    }

    public void setAlbumIconBase64(String albumIconBase64) {
        this.albumIconBase64 = albumIconBase64;
    }

    public Album(String name, LocalDate releaseDate, Integer likesAmount, AlbumState albumState, AlbumGenre albumGenre) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.likesAmount = likesAmount;
        this.albumState = albumState;
        this.albumGenre = albumGenre;
    }


    public Album(Album album) {
        Set<Track> tracksListClone = new HashSet<>();
        for (Track track :
                album.getTrackList()) {
            tracksListClone.add(new Track(track));
        }
        this.artistId = album.getArtistId();
        this.id = album.getId();
        this.name = album.getName();
        this.releaseDate = album.getReleaseDate();
        this.likesAmount = album.getLikesAmount();
        this.albumState = album.getAlbumState();
        this.albumIcon = album.getAlbumIcon();
        this.trackList = tracksListClone;
        this.albumGenre = album.getAlbumGenre();

    }

    public Album(Long id, String name, LocalDate releaseDate, Integer likesAmount, AlbumState albumState, InputStream albumIcon, AlbumGenre albumGenre, String albumIconBase64, Long artistId) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.likesAmount = likesAmount;
        this.albumState = albumState;
        this.albumIcon = albumIcon;
        this.trackList = new HashSet<>();
        this.albumGenre = albumGenre;
        this.artistId = artistId;
        this.albumIconBase64 = albumIconBase64;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id) &&
                Objects.equals(name, album.name) &&
                Objects.equals(releaseDate, album.releaseDate) &&
                Objects.equals(likesAmount, album.likesAmount) &&
                albumState == album.albumState &&
                Objects.equals(albumIcon, album.albumIcon) &&
                Objects.equals(trackList, album.trackList) &&
                albumGenre == album.albumGenre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, releaseDate, likesAmount, albumState, albumIcon, trackList, albumGenre);
    }

    public AlbumGenre getAlbumGenre() {
        return albumGenre;
    }

    public void setAlbumGenre(AlbumGenre albumGenre) {
        this.albumGenre = albumGenre;
    }

    public Set<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(Set<Track> trackList) {
        this.trackList = trackList;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getLikesAmount() {
        return likesAmount;
    }

    public void setLikesAmount(Integer likesAmount) {
        this.likesAmount = likesAmount;
    }

    public AlbumState getAlbumState() {
        return albumState;
    }

    public void setAlbumState(AlbumState albumState) {
        this.albumState = albumState;
    }

    public InputStream getAlbumIcon() {
        return albumIcon;
    }

    public void setAlbumIcon(InputStream albumIcon) {
        this.albumIcon = albumIcon;
    }
}

