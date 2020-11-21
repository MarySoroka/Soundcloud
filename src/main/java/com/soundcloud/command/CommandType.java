package com.soundcloud.command;

/**
 * Command type enum
 */
public enum CommandType {
    GET_PAGE_COMMAND,
    UPDATE_ROLES_COMMAND,
    SUBSCRIBE_USER_COMMAND,
    GET_ADMIN_USER_PAGE_COMMAND,
    DELETE_TRACK_COMMAND,
    UPLOAD_TRACK_COMMAND,
    SEARCH_COMMAND,
    DELETE_USER_COMMAND,
    UNSUBSCRIBE_USER_COMMAND,
    ERROR_COMMAND,
    UNFOLLOW_USER_COMMAND,
    LOGIN_USER_COMMAND,
    LOGOUT_USER_COMMAND,
    REGISTER_USER_COMMAND,
    GET_ALBUM_COMMAND,
    GET_USER_LIBRARY_COMMAND,
    FOLLOW_USER_COMMAND,
    GET_UPLOAD_PAGE_COMMAND,
    DELETE_LIKE_ALBUM_COMMAND,
    EDIT_PROFILE_INFORMATION_COMMAND,
    SAVE_LIKE_ALBUM_COMMAND,
    DELETE_ALBUM_COMMAND,
    UPLOAD_ALBUM_COMMAND,
    EDIT_ALBUM_COMMAND,
    GET_ALL_USER_ARTIST_ALBUMS_COMMAND,
    GET_ALL_USER_ALBUMS_COMMAND,
    REFILL_WALLET_BALANCE_COMMAND,
    GET_FOLLOWER_USERS_COMMAND,
    GET_FOLLOW_USERS_COMMAND,
    GET_LIKED_ALBUMS;

    public static CommandType of(String name) {
        if (name != null) {
            for (CommandType command :
                    CommandType.values()) {
                if (command.name().equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }
        return CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND;
    }

}
