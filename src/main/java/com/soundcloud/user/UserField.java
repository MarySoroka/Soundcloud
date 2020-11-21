package com.soundcloud.user;

import java.util.Optional;
import java.util.stream.Stream;
/**
 * Enum, that represents field in user table
 */
public enum  UserField{
    ID("user_id"),
    LOGIN("user_login"),
    PASSWORD("user_password"),
    EMAIL("user_email"),
    USER_FOLLOW_ID("user_follow_id"),
    USER_ROLES("user_roles"),
    USER_FOLLOWS("user_follows"),
    USER_FOLLOWERS("user_followers"),
    USER_WALLET("wallet_id"),
    USER_ICON("user_icon"),
    FOLLOWS_USER("follows_user"),
    FOLLOWERS_USER("followers_user"),
    USER_ALBUMS("user_albums"),
    USER_LIKED_ALBUMS("user_liked_albums"),
    USER_PLAYLISTS("user_playlists"),
    USER_LIKED_PLAYLISTS("user_liked_playlists");

    public String getField() {
        return field;
    }

    private final String field;
    UserField(String field) {
        this.field =field;
    }
    public static Optional<UserField> of(String name){
        return Stream.of(UserField.values()).filter(t->t.field.equalsIgnoreCase(name)).findFirst();
    }
}
