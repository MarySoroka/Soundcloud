package com.soundcloud.album;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum, that represents field in album table
 */
public enum AlbumField {
    ID("album_id"),
    NAME("album_name"),
    LIKES_AMOUNT("likes_amount"),
    RELEASE_DATE("album_release"),
    ALBUM_STATE("album_state"),
    ALBUM_ICON("album_icon"),
    ALBUM_TRACK_LIST("album_track_list"),
    ALBUM_GENRE("album_genre"),
    ARTIST_ID("artist_id");

    private final String field;

    public String getField() {
        return field;
    }

    AlbumField(String field) {
        this.field =field;
    }
    static Optional<AlbumField> of(String name){
        return Stream.of(AlbumField.values()).filter(t->t.field.equalsIgnoreCase(name)).findFirst();
    }
}
