package com.soundcloud.application;

import java.math.BigDecimal;

/**
 * Singleton, that keeps all application constants
 */
public class ApplicationConstants {


    private ApplicationConstants() {
    }

    public static final String DATABASE_PROPERTY_FILE = "database.properties";
    public static final String PROPERTY_FILE_NOT_FOUND_EXCEPTION = "Property file '0' not found in the classpath";

    public static final String DAO_EXECUTE_EXCEPTION = "Couldn't execute query: 0";
    public static final String DAO_SAVE_EXCEPTION = "Couldn't save query: 0";
    public static final String DAO_UPDATE_EXCEPTION = "Couldn't update query: 0";
    public static final String DAO_DELETE_EXCEPTION = "Couldn't delete query: 0";
    public static final String DAO_ASSIGN_EXCEPTION = "Couldn't assign query: 0";

    public static final String BUILD_EXCEPTION = "Couldn't build: 0";

    public static final String SERVICE_EXCEPTION = "Service exception: 0";
    public static final String SERVICE_ALBUM_SAVE_EXCEPTION = "Couldn't save artist album";
    public static final String SERVICE_FIND_EXCEPTION = "Couldn't find user by id";
    public static final String SERVICE_FATAL_EXCEPTION = "Service fatal exception: 0";

    public static final String CONNECTION_POOL_EXCEPTION = "Connection pool exception: 0";
    public static final String CONNECTION_POOL_FATAL_EXCEPTION = "Connection pool fatal exception: 0";
    public static final String DATA_SOURCE_EXCEPTION = "Data source exception: 0";
    public static final String DESTROY_POOL_EXCEPTION = "Can't destroy pool: 0";
    public static final String TAKE_CONNECTION_EXCEPTION = "Can't take connection: 0";
    public static final String TRANSACTION_EXCEPTION = "Transaction error: 0";

    public static final String COMMAND_UPDATE_ROLES_EXCEPTION = "Couldn't update user roles: 0";
    public static final String COMMAND_UPDATE_ROLES_FATAL_EXCEPTION = "Couldn't update user roles: fatal exception: 0 ";
    public static final String COMMAND_PERMISSION_EXCEPTION ="This user don't have permission to execute command: {}";
    public static final String COMMAND_CREATE_FILE_EXCEPTION = "Couldn't create audio file";
    public static final String COMMAND_TRACK_SIZE_EXCEPTION ="Size of track should be less then 16 Mb";
    public static final String COMMAND_IMAGE_SIZE_EXCEPTION ="Size of track should be less then 10 Mb";
    public static final String COMMAND_MAX_TRACKS_AMOUNT_EXCEPTION ="Amount of tracks should be less then 9";
    public static final String COMMAND_EXCEPTION = "Couldn't execute command";
    public static final String COMMAND_REGISTER_EXCEPTION = "Register exception: 0";
    public static final String COMMAND_DELETE_EXCEPTION = "Couldn't delete 0";
    public static final String COMMAND_REGISTER_FATAL_EXCEPTION = "Couldn't register user";
    public static final String COMMAND_LOGIN_EXCEPTION = "Login or password is not valid";
    public static final String COMMAND_LOGOUT_EXCEPTION = "Can't logout user";
    public static final String COMMAND_USER_PRESENTS="User with this login presents";
    public static final String COMMAND_FOLLOW_EXCEPTION = "Can't follow user";
    public static final String COMMAND_LIKE_EXCEPTION = "Can't like album: 0";
    public static final String COMMAND_GET_ALBUM_EXCEPTION = "Can't get album : 0";
    public static final String COMMAND_GET_ALL_ALBUMS_EXCEPTION = "Can't get all albums : 0";
    public static final String COMMAND_UPLOAD_FATAL_EXCEPTION = "Upload command fatal exception: 0";
    public static final String COMMAND_UPLOAD_EXCEPTION = "Command upload exception: 0";
    public static final String COMMAND_UPLOAD_CLIENT_EXCEPTION = "Couldn't upload album: image or tracks size is too big";
    public static final String COMMAND_UPLOAD_SAVE_FILE_EXCEPTION = "Couldn't save file exception: 0";
    public static final String COMMAND_UPLOAD_FILE_DIR = "/opt/tomcat/webapps/soundcloud_tracks/";
    public static final String COMMAND_TRACK_FILE_DIR = "/soundcloud_tracks/";
    public static final String COMMAND_UPLOAD_SUCCESS = "Upload has been done successfully!";
    public static final String COMMAND_SUBSCRIBE_EXCEPTION = "Insufficient funds";
    public static final String COMMAND_SUBSCRIBE_FATAL_EXCEPTION = "Subscribe fatal exception: 0";
    public static final String COMMAND_SEARCH_EXCEPTION = "Sorry we didn't find any results.\n" +"Check the spelling, or try a different search.";

    public static final BigDecimal SUBSCRIBE_AMOUNT = new BigDecimal(8);

    public static final String COMMAND_NAME_PARAM = "command";
    public static final String REQUEST_AUTHORIZED_ATTRIBUTE = "authorized";
    public static final String REQUEST_ERROR_ATTRIBUTE = "errors";
    public static final String REQUEST_PARAMETER_PASSWORD = "password";
    public static final String REQUEST_COMMAND_SUCCESS_MESSAGE = "message";
    public static final String REQUEST_PARAMETER_USER_ROLE = "userRole";
    public static final String REQUEST_PARAMETER_EMAIL = "email";
    public static final String REQUEST_PARAMETER_PAGE = "page";
    public static final String REQUEST_PARAMETER_IMAGE = "image";
    public static final String REQUEST_PARAMETER_IS_LIKED = "isLiked";
    public static final String REQUEST_PARAMETER_IS_ADMIN = "isAdmin";
    public static final String REQUEST_PARAMETER_USER_ID = "userId";
    public static final String REQUEST_PARAMETER_ALBUM_ID = "albumId";
    public static final String REQUEST_PARAMETER_ALBUM = "album";
    public static final String REQUEST_PARAMETER_ALBUM_DATE = "album_release_date";
    public static final String REQUEST_PARAMETER_ALBUM_IS_OWN = "isOwn";
    public static final String REQUEST_PARAMETER_USER_NAME = "userName";
    public static final String REQUEST_PARAMETER_USER = "user";
    public static final String REQUEST_FOLLOW_USER_ID = "follow_user_id";
    public static final String REQUEST_ARTIST_ID = "artist_id";
    public static final String REQUEST_FOLLOWER_USER_AMOUNT = "follower_amount";
    public static final String REQUEST_PARAMETER_LOGIN = "login";
    public static final String REQUEST_PARAMETER_ALBUM_NAME = "album_name";
    public static final String REQUEST_PARAMETER_ALBUM_GENRE = "album_genre";
    public static final String REQUEST_PARAMETER_ALBUM_STATE = "album_state";
    public static final String REQUEST_PARAMETER_TRACK_ID = "track_id";
    public static final String REQUEST_PARAMETER_ALBUM_ICON = "album_icon";
    public static final String REQUEST_PARAMETER_USERS="users";
    public static final String REQUEST_PARAMETER_NOT_FOLLOW_USERS="notFollowUsers";
    public static final String REQUEST_PARAMETER_IS_CURRENT_USER="isCurrentUser";
    public static final String REQUEST_PARAMETER_ALBUM_LIKES_AMOUNT = "likesAmount";
    public static final String REQUEST_PARAMETER_ALBUM_LIKED = "likedAlbums";
    public static final String REQUEST_PARAMETER_AUDIO = "audio";
    public static final String REQUEST_PARAMETER_WALLET_AMOUNT = "walletAmount";
    public static final String REQUEST_PARAMETER_SUBSCRIBE_AMOUNT = "subscribeAmount";
    public static final String REQUEST_PARAMETER_SUBSCRIBED = "subscribe";
    public static final String REQUEST_PARAMETER_SUBSCRIPTION = "subscription";
    public static final String REQUEST_PARAMETER_WALLET_ID = "walletId";
    public static final String REQUEST_PARAMETER_SEARCH_DATA = "searchData";
    public static final String REQUEST_PARAMETER_FIND_ALBUMS = "";
    public static final String REQUEST_PARAMETER_FIND_TRACKS = "searchData";
    public static final String REQUEST_PARAMETER_FIND_USER = "searchData";


    public static final String VALIDATION_IS_EMPTY_EXCEPTION = "Fill field, because it is null or empty";
    public static final String VALIDATION_MIN_LENGTH_EXCEPTION = "Length is less then 0";
    public static final String VALIDATION_MAX_LENGTH_EXCEPTION = "Length is more then 0";
    public static final String VALIDATION_REGEX_EXCEPTION = "1 is not match to the 1 regex 0";
    public static final String VALIDATION_FATAL_EXCEPTION = "Couldn't validate field: fatal error : 0";
    public static final String VALIDATION_ANNOTATION_NOT_FOUND_EXCEPTION = "Couldn't validate bean : annotation not found or null";
    public static final String VALIDATION_EMAIL_REGEX = "^[A-Z0-9._]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String VALIDATION_LOGIN_REGEX = "^[a-zA-Z0-9._-]{3,}$";


    public static final String PROPERTY_COMMAND = "command.";
    public static final String SECURITY_PROPERTY_FILE = "/security.properties";
    public static final int APPLICATION_INITIAL_CAPACITY = 1000;

    public static final String LISTENER_CONTEXT_INITIALIZE = "0 was initialized";
    public static final String LISTENER_CONTEXT_DESTROYED = "0 was destroyed";

    public static final String ENCODING_ALGORITHM = "SHA-256";
    public static final String PASSWORD_ENCODE_STATE = "Password 0 encoded";

    public static final Long TRACK_MAX_SIZE = 15728640L;
    public static final Long IMAGE_MAX_SIZE = 10485760L;
    public static final Integer MAX_TRACKS_AMOUNT = 8;


}
