package com.soundcloud.subscription;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents field in subscription table
 */
public enum  SubscriptionField {
    ID("subscription_id"),
    SUBSCRIPTION_DATE("subscription_date"),
    SUBSCRIPTION_STATUS("subscription_status"),
    SUBSCRIPTION_USER_ID("subscription_user_id");

    private final String field;
    SubscriptionField(String field) {
        this.field = field;
    }
    public static Optional<SubscriptionField> of(String name) {
        return Stream.of(SubscriptionField.values()).filter(t -> t.field.equalsIgnoreCase(name)).findFirst();
    }

    public String getField() {
        return field;
    }
}
