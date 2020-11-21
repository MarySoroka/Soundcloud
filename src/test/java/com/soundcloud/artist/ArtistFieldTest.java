package com.soundcloud.artist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ArtistFieldTest {
    @Test
    public void getByNameArtistFieldDefaultIsEmpty() {
        String type = "DEFAULT";
        Optional<ArtistField> artistField = ArtistField.of(type);
        assertFalse(artistField.isPresent());
    }
    @Test
    public void getByNameArtistFieldUserIdIsEmpty() {
        String type = "user_id";
        Optional<ArtistField> artistField = ArtistField.of(type);
        assertTrue(artistField.isPresent());
    }
    @Test
    public void getByNameArtistFieldArtistIdIsEmpty() {
        String type = "artist_id";
        Optional<ArtistField> artistField = ArtistField.of(type);
        assertTrue(artistField.isPresent());
    }
}
