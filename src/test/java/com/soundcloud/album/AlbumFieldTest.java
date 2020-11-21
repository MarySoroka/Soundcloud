package com.soundcloud.album;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AlbumFieldTest {
    @Test
    public void getByNameAlbumFieldDefaultIsEmpty() {
        String type = "DEFAULT";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertFalse(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumIdIsPresent() {
        String type = "album_id";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumNameIsPresent() {
        String type = "album_name";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameUserFieldAlbumReleaseIsPresent() {
        String type = "album_release";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumStateIsPresent() {
        String type = "album_state";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumGenreIsPresent() {
        String type = "album_genre";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumIconIsPresent() {
        String type = "album_icon";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumTrackListIsPresent() {
        String type = "album_track_list";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }
    @Test
    public void getByNameAlbumFieldAlbumArtistIdIsPresent() {
        String type = "artist_id";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }

    @Test
    public void getByNameAlbumFieldAlbumLikesAmountIsPresent() {
        String type = "likes_amount";
        Optional<AlbumField> albumField = AlbumField.of(type);
        assertTrue(albumField.isPresent());
    }




}
