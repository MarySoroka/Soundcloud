package com.soundcloud.role;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents field in role table
 */
public enum RoleField {
    ID("role_id"),
    USER_ID("user_id"),
    NAME("role_name");
    private final String field;

    RoleField(String field) {
        this.field = field;
    }

    public static Optional<RoleField> of(String name) {
        return Stream.of(RoleField.values()).filter(t -> t.field.equalsIgnoreCase(name)).findFirst();
    }

    public String getField() {
        return field;
    }
}
