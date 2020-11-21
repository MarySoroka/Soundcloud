package com.soundcloud.track;

import java.util.Objects;

public class Track  {
    private Long id;
    private String name;
    private Integer playsAmount;
    private Long albumId;

    public Track(String name, Integer playsAmount, String trackPath) {
        this.name = name;
        this.playsAmount = playsAmount;
        this.trackPath = trackPath;
    }

    public String getTrackPath() {
        return trackPath;
    }

    public void setTrackPath(String trackPath) {
        this.trackPath = trackPath;
    }

    private String trackPath;


    public Track(Long id, String name, Integer playsAmount, Long albumId, String trackPath) {
        this.id = id;
        this.name = name;
        this.playsAmount = playsAmount;
        this.albumId = albumId;
        this.trackPath = trackPath;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Track(Track track) {
        this.id = track.getId();
        this.albumId = track.getAlbumId();
        this.name= track.getName();
        this.playsAmount = track.getPlaysAmount();
        this.trackPath = track.getTrackPath();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track that = (Track) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(playsAmount, that.playsAmount) &&
                Objects.equals(albumId, that.albumId) &&
                Objects.equals(trackPath, that.trackPath)
        ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, playsAmount, albumId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlaysAmount() {
        return playsAmount;
    }

    public void setPlaysAmount(Integer playsAmount) {
        this.playsAmount = playsAmount;
    }
}
