package com.soundcloud.role;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents role types
 */
public enum RoleType {
    NOT_AUTHORIZED("not_authorized",0L),
    ADMIN("admin", 1L),
    SINGED_USER("signed_user", 3L),
    UNSIGNED_USER("unsigned_user", 2L);

    private static final Logger LOGGER = LogManager.getLogger(RoleType.class);
    private final String type;
    private final Long roleTypeId;

    RoleType(String type, long roleTypeId) {
        this.type = type;
        this.roleTypeId = roleTypeId;
    }

    public static RoleType of(String role) {
        Optional<RoleType> roleType = Stream.of(RoleType.values())
                .filter(c -> c.type.equalsIgnoreCase(role))
                .findFirst();
        if (!roleType.isPresent()) {
            LOGGER.error("Couldn't find role by this type {}", role);
            return RoleType.UNSIGNED_USER;
        }
        return roleType.get();
    }

    public static RoleType of(Long role) {
        Optional<RoleType> roleType = Stream.of(RoleType.values())
                .filter(c -> c.roleTypeId.compareTo(role) == 0)
                .findFirst();
        if (!roleType.isPresent()) {
            LOGGER.error("Couldn't find role by this id {}", role);
            return RoleType.UNSIGNED_USER;
        }
        return roleType.get();
    }

    public Long getRoleTypeId() {
        return roleTypeId;
    }

    public String getType() {
        return type;
    }
}
