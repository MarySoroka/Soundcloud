package com.soundcloud.track;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents field in track table
 */
public enum TrackField {
    ID("track_id"),
    NAME("track_name"),
    PLAYS_AMOUNT("plays_amount"),
    TRACK_PATH("track_path"),
    ALBUM_ID("album_id");
    private final String field;

    public String getField() {
        return field;
    }

    TrackField(String field) {
        this.field = field;
    }

    static Optional<TrackField> of(String name) {
        return Stream.of(TrackField.values()).filter(t -> t.field.equalsIgnoreCase(name)).findFirst();
    }
}
