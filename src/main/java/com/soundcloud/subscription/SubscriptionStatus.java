package com.soundcloud.subscription;

import com.soundcloud.album.AlbumGenre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents subscription's status
 */
public enum SubscriptionStatus {
    ACTIVE("ACTIVE"),
    NOT_ACTIVE("NOT_ACTIVE");
    private final String field;
    private static final Logger LOGGER = LogManager.getLogger(AlbumGenre.class);

    public String getField() {
        return field;
    }

    SubscriptionStatus(String field) {
        this.field = field;
    }

    public static SubscriptionStatus of(String genre) {
        Optional<SubscriptionStatus> subscriptionStatus = Stream.of(SubscriptionStatus.values())
                .filter(c -> c.field.equalsIgnoreCase(genre))
                .findFirst();
        if (!subscriptionStatus.isPresent()) {
            LOGGER.error("Couldn't find status by this type {}", genre);
            return SubscriptionStatus.NOT_ACTIVE;
        }
        return subscriptionStatus.get();
    }
}
