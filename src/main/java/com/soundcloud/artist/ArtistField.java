package com.soundcloud.artist;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum, that represents field in artist table
 */
public enum  ArtistField {
    USER_ID("user_id"),
    ARTIST_ID("artist_id");

    private final String field;

    public String getField() {
        return field;
    }

    ArtistField(String field) {
        this.field =field;
    }
    static Optional<ArtistField> of(String name){
        return Stream.of(ArtistField.values()).filter(t->t.field.equalsIgnoreCase(name)).findFirst();
    }
}
