package com.soundcloud.album;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum, that represents album genre
 */
public enum AlbumGenre {
    
    ROCK("rock"),
    ELECTRONIC("electronic"),
    SOUL("soul"),
    FUNK("funk"),
    COUNTRY("country"),
    LATIN("latin"),
    REGGAE("reggae"),
    HIP_HOP("hip_hop");
    private final String field;
    private static final Logger LOGGER = LogManager.getLogger(AlbumGenre.class);

    public String getField() {
        return field;
    }

    AlbumGenre(String field) {
        this.field =field;
    }
    public static AlbumGenre of(String genre) {
        Optional<AlbumGenre> albumGenre = Stream.of(AlbumGenre.values())
                .filter(c -> c.field.equalsIgnoreCase(genre))
                .findFirst();
        if (!albumGenre.isPresent()) {
            LOGGER.error("Couldn't find genre by this type {}", genre);
            return AlbumGenre.ELECTRONIC;
        }
        return albumGenre.get();
    }
}
