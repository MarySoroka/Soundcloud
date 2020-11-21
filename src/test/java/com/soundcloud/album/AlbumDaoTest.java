package com.soundcloud.album;

import com.soundcloud.TestUtils;
import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.TestUtils.createDB;
import static com.soundcloud.application.ApplicationConstants.BUILD_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.DAO_EXECUTE_EXCEPTION;

@RunWith(JUnit4.class)
public class AlbumDaoTest {
    private static final Logger LOGGER = LogManager.getLogger(AlbumDaoTest.class);

    static {
        ApplicationContext.initialize();
    }

    private static final String SELECT_ALL_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud_test.album";
    private static final String SELECT_BY_ID_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud_test.album where album_id = ?";
    private static final String SELECT_BY_NAME_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud_test.album where album_name = ?";
    private static final String SELECT_ALBUM_BY_ARTIST_ID_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud_test.album where artist_id = ?";
    private static final String SELECT_LIKED_ALBUMS_QUERY = "select liked_user_albums.album_id, album.album_id, album.album_name,album.likes_amount, album.album_release, album.album_icon, album.album_state, album.album_genre, album.artist_id  from soundcloud_test.liked_user_albums join soundcloud_test.album on liked_user_albums.album_id=album.album_id where liked_user_albums.user_id=?";
    private static final String SELECT_IS_LIKED_ALBUMS_QUERY = "select liked_user_albums.album_id from soundcloud_test.liked_user_albums where liked_user_albums.user_id=? and liked_user_albums.album_id=? ";

    private static final String INSERT_QUERY = "insert into soundcloud_test.album ( album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id) values (?,?,?,?,?,?,?)";
    private static final String INSERT_LIKE_ALBUM_QUERY = "insert into soundcloud_test.liked_user_albums (album_id, user_id) values (?,?)";

    private static final String DELETE_LIKE_ALBUM_QUERY = "delete from soundcloud_test.liked_user_albums where album_id = ? and user_id=?";
    private static final String UPDATE_QUERY = "update soundcloud_test.album set album_name=?, album_state=?, album_genre=? where album_id = ?";
    private static final String UPDATE_WITH_ICON_QUERY = "update soundcloud_test.album set album_name=?, album_icon=?, album_state=?, album_genre=? where album_id = ?";

    private static final String UPDATE_LIKES_AMOUNT = "update soundcloud_test.album set likes_amount=? where album_id = ?";

    private static final String DELETE_QUERY = "delete from soundcloud_test.album where album_id = ?";
    private final ConnectionManager connectionManager= ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);;
    private Album album;

    @BeforeClass
    public static void init() throws SQLException, ConnectionManagerException {
        ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();
        createDB(connection);



    }
    @Before
    public void initAlbum() throws SQLException, ConnectionManagerException {
        album = new Album("la", LocalDate.now(ZoneId.of("America/Montreal")), 0, AlbumState.ALBUM, AlbumGenre.COUNTRY);
        album.setAlbumIcon(this.getClass().getResourceAsStream("/avatar.png"));
        album.setArtistId(1L);
        album.setId(1L);
        save();
    }

    @Test
    public void delete() throws ConnectionManagerException, SQLException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, album.getId());
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }


    @Test
    public void update() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            updateStatement.setString(++i, album.getName());
            updateStatement.setString(++i, album.getAlbumState().name());
            updateStatement.setString(++i, album.getAlbumGenre().name());
            updateStatement.setLong(++i, album.getId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }
    }

    public void save() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(album, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            long albumId = generatedKeys.getLong(1);
            album.setId(albumId);
        }

    }

    private void setStatement(Album album, PreparedStatement statement, int i) throws SQLException {
        statement.setString(++i, album.getName());
        statement.setInt(++i, album.getLikesAmount());
        statement.setDate(++i, java.sql.Date.valueOf(album.getReleaseDate()));
        statement.setBlob(++i, album.getAlbumIcon());
        statement.setString(++i, album.getAlbumState().name());
        statement.setString(++i, album.getAlbumGenre().name());
        statement.setLong(++i, album.getArtistId());

    }

    @Test
    public void getById() throws DaoException, SQLException, ConnectionManagerException, BuildException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, album.getId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            Assert.assertTrue(build(resultSet).isPresent());
        }
    }

    @Test
    public void getAll() throws DaoException, BuildException, SQLException, ConnectionManagerException {
        List<Album> albums = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Album> user = build(resultSet);
                user.ifPresent(albums::add);
            }
            Assert.assertTrue(albums.size()>0);
        }
    }


    public Optional<Album> build(ResultSet data) throws BuildException, DaoException {
        EntityBuilder<Album> entityBuilder = new AlbumBuilder();
        try {
            Album album = entityBuilder.build(data);
            return Optional.of(album);
        } catch (SQLException e) {
            LOGGER.error(BUILD_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new BuildException(BUILD_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }



    public void saveLikedAlbum() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertArtist = connection.prepareStatement(INSERT_LIKE_ALBUM_QUERY)) {
            int i = 0;
            insertArtist.setLong(++i, album.getId());
            insertArtist.setLong(++i, 1L);
            Assert.assertTrue(insertArtist.executeUpdate() > 0);
        }
    }

    @Test
    public void isLiked() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_IS_LIKED_ALBUMS_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            findByIdStatement.setLong(++i, 1L);
            findByIdStatement.setLong(++i, album.getId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            Assert.assertFalse(resultSet.next());
        }
    }

    @Test
    public void deleteLikedAlbum() throws SQLException, ConnectionManagerException {
        saveLikedAlbum();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertArtist = connection.prepareStatement(DELETE_LIKE_ALBUM_QUERY)) {
            int i = 0;
            insertArtist.setLong(++i, album.getId());
            insertArtist.setLong(++i, 1L);
            Assert.assertTrue(insertArtist.executeUpdate() > 0);
        }
    }

    @Test
    public void getArtistAlbum() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_ALBUM_BY_ARTIST_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, album.getArtistId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            Assert.assertTrue(resultSet.next());
        }
    }


    @Test
    public void getAlbumByName() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByNameStatement = connection.prepareStatement(SELECT_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByNameStatement.setString(1, album.getName());
            ResultSet resultSet = findByNameStatement.executeQuery();
            Assert.assertTrue(resultSet.next());
        }
    }

    @Test
    public void getLikedAlbums() throws SQLException, ConnectionManagerException {
       saveLikedAlbum();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_LIKED_ALBUMS_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            List<Album> albums = new LinkedList<>();
            findByIdStatement.setLong(1, 1L);
            ResultSet resultSet = findByIdStatement.executeQuery();
            Assert.assertTrue(resultSet.next());
        }
    }

    @Test
    public void updateLikesAmount() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_LIKES_AMOUNT)) {
            int i = 0;
            updateStatement.setInt(++i, album.getLikesAmount());
            updateStatement.setLong(++i, album.getId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }
    }

    @Test
    public void updateWithIcon() throws SQLException, ConnectionManagerException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_WITH_ICON_QUERY)) {
            int i = 0;
            updateStatement.setString(++i, album.getName());
            updateStatement.setBlob(++i, album.getAlbumIcon());
            updateStatement.setString(++i, album.getAlbumState().name());
            updateStatement.setString(++i, album.getAlbumGenre().name());
            updateStatement.setLong(++i, album.getId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }
    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }

}
