package com.soundcloud.track;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TrackFieldTest {
    @Test
    public void getByNameTrackFieldDefaultIsEmpty() {
        String type = "DEFAULT";
        Optional<TrackField> trackField = TrackField.of(type);
        assertFalse(trackField.isPresent());
    }
    @Test
    public void getByNameTrackFieldIdIsPresent() {
        String type = "track_id";
        Optional<TrackField> trackField = TrackField.of(type);
        assertTrue(trackField.isPresent());
    }
    @Test
    public void getByNameTrackFieldNameIsPresent() {
        String type = "track_name";
        Optional<TrackField> trackField = TrackField.of(type);
        assertTrue(trackField.isPresent());
    }
    @Test
    public void getByNameTrackFieldPathIsPresent() {
        String type = "track_path";
        Optional<TrackField> trackField = TrackField.of(type);
        assertTrue(trackField.isPresent());
    }
    @Test
    public void getByNameTrackFieldAlbumIdIsPresent() {
        String type = "album_id";
        Optional<TrackField> trackField = TrackField.of(type);
        assertTrue(trackField.isPresent());
    }
    @Test
    public void getByNameTrackFieldPlaysAmountIsPresent() {
        String type = "plays_amount";
        Optional<TrackField> trackField = TrackField.of(type);
        assertTrue(trackField.isPresent());
    }
}
