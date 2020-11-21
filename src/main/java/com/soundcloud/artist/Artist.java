package com.soundcloud.artist;

import java.util.Objects;

public class Artist {
    private Long artistId;
    private Long userId;

    public Artist(Artist artist) {
        this.artistId = artist.getArtistId();
        this.userId = artist.getUserId();
    }
    public Artist(Long artistId, Long userId) {
        this.artistId = artistId;
        this.userId = userId;
    }

    public Artist(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(artistId, artist.artistId) &&
                Objects.equals(userId, artist.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, userId);
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
