package com.soundcloud.album;

import com.soundcloud.bean.Bean;
import com.soundcloud.bean.BeanQualifier;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.DaoQueryExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.*;

/**
 * Class, that implements AlbumDao interface and is used for database interaction
 */
@Bean(beanName = "albumDaoImpl")
public class AlbumDaoImpl implements AlbumDao {

    private static final Logger LOGGER = LogManager.getLogger(AlbumDaoImpl.class);
    private static final String SELECT_ALL_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud.album";
    private static final String SELECT_BY_ID_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud.album where album_id = ?";
    private static final String SELECT_BY_NAME_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud.album where album_name = ?";
    private static final String FIND_BY_NAME_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud.album where album_name COLLATE UTF8_GENERAL_CI like ?";

    private static final String SELECT_ALBUM_BY_ARTIST_ID_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud.album where artist_id = ?";
    private static final String SELECT_LIKED_ALBUMS_QUERY = "select liked_user_albums.album_id, album.album_id, album.album_name,album.likes_amount, album.album_release, album.album_icon, album.album_state, album.album_genre, album.artist_id  from soundcloud.liked_user_albums join soundcloud.album on liked_user_albums.album_id=album.album_id where liked_user_albums.user_id=?";
    private static final String SELECT_IS_LIKED_ALBUMS_QUERY = "select liked_user_albums.album_id from soundcloud.liked_user_albums where liked_user_albums.user_id=? and liked_user_albums.album_id=? ";

    private static final String INSERT_QUERY = "insert into soundcloud.album ( album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id) values (?,?,?,?,?,?,?)";
    private static final String INSERT_LIKE_ALBUM_QUERY = "insert into soundcloud.liked_user_albums (album_id, user_id) values (?,?)";

    private static final String DELETE_LIKE_ALBUM_QUERY = "delete from soundcloud.liked_user_albums where album_id = ? and user_id=?";
    private static final String UPDATE_QUERY = "update soundcloud.album set album_name=?, album_state=?, album_genre=? where album_id = ?";
    private static final String UPDATE_WITH_ICON_QUERY = "update soundcloud.album set album_name=?, album_icon=?, album_state=?, album_genre=? where album_id = ?";

    private static final String UPDATE_LIKES_AMOUNT = "update soundcloud.album set likes_amount=? where album_id = ?";

    private static final String DELETE_QUERY = "delete from soundcloud.album where album_id = ?";


    private final EntityBuilder<Album> entityBuilder;
    private final ConnectionManager connectionManager;

    public AlbumDaoImpl(ConnectionManager connectionManager, @BeanQualifier(value = "albumBuilder") EntityBuilder<Album> entityBuilder) {
        this.connectionManager = connectionManager;
        this.entityBuilder = entityBuilder;
    }

    /**
     * method delete album in album table in database by album id
     *
     * @param key album id
     * @return true, if album has been deleted,else false
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean delete(Long key) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, key);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method update album in album table in database by album id
     *
     * @param key   album id
     * @param album album entity that is used for update album in database
     * @return true, if album has been updated,else false
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean update(Long key, Album album) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            updateStatement.setString(++i, album.getName());
            updateStatement.setString(++i, album.getAlbumState().name());
            updateStatement.setString(++i, album.getAlbumGenre().name());
            updateStatement.setLong(++i, album.getId());
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    /**
     * method save album in album table in database by album id
     *
     * @param album album entity that is used for update album in database
     * @return album id, that has been generated in database
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public Long save(Album album) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(album, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            long albumId = generatedKeys.getLong(1);
            album.setId(albumId);
            return albumId;
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    /**
     * set in prepared statement album values
     *
     * @param album     album, that is used to get values
     * @param statement prepared statement for sql request
     * @param i         counter
     * @throws SQLException if we get exception in sql request
     */
    private void setStatement(Album album, PreparedStatement statement, int i) throws SQLException {
        statement.setString(++i, album.getName());
        statement.setInt(++i, album.getLikesAmount());
        statement.setDate(++i, java.sql.Date.valueOf(album.getReleaseDate()));
        statement.setBlob(++i, album.getAlbumIcon());
        statement.setString(++i, album.getAlbumState().name());
        statement.setString(++i, album.getAlbumGenre().name());
        statement.setLong(++i, album.getArtistId());

    }

    /**
     * method get album by id in album table in database by album id
     *
     * @param id album id
     * @return if album has been found, return optional of album, else Optional.empty()
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public Optional<Album> getById(Long id) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            return build(resultSet);
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method get all albums in album table in database by album id
     *
     * @return get all albums from album table
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public List<Album> getAll() throws DaoException {
        List<Album> albums = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Album> user = build(resultSet);
                user.ifPresent(albums::add);
            }
            return albums;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method clone album
     *
     * @param album album entity that is used for clone
     * @return new Album object
     */
    @Override
    public Album clone(Album album) {
        return new Album(album);
    }

    /**
     * Build new Album object from result set, that we get from database
     *
     * @param data set, that is used to build album object
     * @return Album object
     * @throws BuildException if we couldn't find any field by field type or blob file
     * @throws DaoException   if we get exception in sql request
     */
    @Override
    public Optional<Album> build(ResultSet data) throws BuildException, DaoException {
        try {
            Album album = entityBuilder.build(data);
            return Optional.of(album);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method save album in album table in database by album id
     *
     * @param albumId album id
     * @param userId  userId, that liked this album
     * @return album id, that has been generated in database
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean saveLikedAlbum(Long albumId, Long userId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertArtist = connection.prepareStatement(INSERT_LIKE_ALBUM_QUERY)) {
            int i = 0;
            insertArtist.setLong(++i, albumId);
            insertArtist.setLong(++i, userId);
            return insertArtist.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method like album in album table in database by album id
     *
     * @param albumId album id
     * @param userId  userId, that liked this album
     * @return if album is liked - true, else false
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean isLiked(Long userId, Long albumId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_IS_LIKED_ALBUMS_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            findByIdStatement.setLong(++i, userId);
            findByIdStatement.setLong(++i, albumId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method delete album in album table in database by album id
     *
     * @param albumId album id
     * @param userId  userId, that liked this album
     * @return if album was deleted - true, else false
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean deleteLikedAlbum(Long albumId, Long userId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertArtist = connection.prepareStatement(DELETE_LIKE_ALBUM_QUERY)) {
            int i = 0;
            insertArtist.setLong(++i, albumId);
            insertArtist.setLong(++i, userId);
            return insertArtist.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method get artist album by artist id from album table
     *
     * @param artistId artist id
     * @return list of artist albums
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public List<Album> getArtistAlbum(Long artistId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_ALBUM_BY_ARTIST_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            List<Album> albums = new LinkedList<>();
            findByIdStatement.setLong(1, artistId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Album> user = build(resultSet);
                user.ifPresent(albums::add);
            }
            return albums;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method save album in album table in database by album id
     *
     * @param albumName album name
     * @return list of artist albums
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public List<Album> getAlbumByName(String albumName) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByNameStatement = connection.prepareStatement(SELECT_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            List<Album> albums = new LinkedList<>();
            findByNameStatement.setString(1, albumName);
            ResultSet resultSet = findByNameStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Album> user = build(resultSet);
                user.ifPresent(albums::add);
            }
            return albums;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method find album in album table in database by albumName
     *
     * @param albumName album name
     * @return albums that name contains name of equals
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public List<Album> findAlbumByName(String albumName) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByNameStatement = connection.prepareStatement(FIND_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            List<Album> albums = new LinkedList<>();
            findByNameStatement.setString(1, "%" + albumName + "%");
            ResultSet resultSet = findByNameStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Album> album = build(resultSet);
                album.ifPresent(albums::add);
            }
            return albums;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method get user liked albums in album table in database by albumName
     *
     * @param userId user id
     * @return user liked albums or empty list, if this are no liked albums
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public List<Album> getLikedAlbums(Long userId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_LIKED_ALBUMS_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            List<Album> albums = new LinkedList<>();
            findByIdStatement.setLong(1, userId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Album> album = build(resultSet);
                album.ifPresent(albums::add);
            }
            return albums;
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        } catch (BuildException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method set new value of amount of album likes
     *
     * @param albumId     album id
     * @param likesAmount amount of albums likes
     * @return if amount has been updated - true, else false
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean updateLikesAmount(Long albumId, Integer likesAmount) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_LIKES_AMOUNT)) {
            int i = 0;
            updateStatement.setInt(++i, likesAmount);
            updateStatement.setLong(++i, albumId);
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

    /**
     * method update album information in album table by album id
     *
     * @param albumId album id
     * @param album   album object
     * @return if amount has been updated - true, else false
     * @throws DaoException if we get exception in sql request
     */
    @Override
    public boolean updateWithIcon(Long albumId, Album album) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_WITH_ICON_QUERY)) {
            int i = 0;
            updateStatement.setString(++i, album.getName());
            updateStatement.setBlob(++i, album.getAlbumIcon());
            updateStatement.setString(++i, album.getAlbumState().name());
            updateStatement.setString(++i, album.getAlbumGenre().name());
            updateStatement.setLong(++i, album.getId());
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }
    }

}
