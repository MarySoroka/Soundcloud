package com.soundcloud.album;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents album state
 */
public enum AlbumState {
    SINGLE("single"),
    ALBUM("album"),
    REMIXES("remixes"),
    MIXTAPE("mixtape"),
    SOUNDTRACK("soundtrack"),
    LIVE("live"),
    BOOTLEG("bootleg"),
    PROMO("promo");

    private final String field;
    private static final Logger LOGGER = LogManager.getLogger(AlbumState.class);
    public String getField() {
        return field;
    }

    AlbumState(String field) {
        this.field =field;
    }

    /**
     *
     * @param state album state
     * @return state by satne name
     */
    public static AlbumState of(String state) {
        Optional<AlbumState> albumGenre = Stream.of(AlbumState.values())
                .filter(c -> c.field.equalsIgnoreCase(state))
                .findFirst();
        if (!albumGenre.isPresent()) {
            LOGGER.error("Couldn't find state by this type {}", state);
            return AlbumState.SINGLE;
        }
        return albumGenre.get();
    }
}
