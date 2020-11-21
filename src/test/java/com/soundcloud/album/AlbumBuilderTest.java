package com.soundcloud.album;

import com.soundcloud.TestUtils;
import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DaoQueryExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.soundcloud.TestUtils.createDB;
import static com.soundcloud.user.UserDaoTest.SAVE_EXCEPTION;

@RunWith(JUnit4.class)
public class AlbumBuilderTest {
    private static final String SELECT_ALL_QUERY = "select album_id, album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id from soundcloud_test.album";
    private static final String INSERT_QUERY = "insert into soundcloud_test.album ( album_name,likes_amount, album_release, album_icon, album_state, album_genre, artist_id) values (?,?,?,?,?,?,?)";
    private static final Logger LOGGER = LogManager.getLogger(AlbumBuilderTest.class);
    private Album album;
    private ConnectionManager connectionManager;

    public void save() throws DaoQueryExecuteException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(album, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            Long id = generatedKeys.getLong(1);
            LOGGER.info("id {}", id);
            Assert.assertNotNull(id);
        } catch (SQLException | ConnectionManagerException e) {
            LOGGER.error(SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(SAVE_EXCEPTION.replace("0", ""), e);
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

    @Before
    public void init() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {

        ApplicationContext.initialize();
        connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();

        createDB(connection);
        album = new Album("la", LocalDate.now(ZoneId.of("America/Montreal")), 0, AlbumState.ALBUM, AlbumGenre.COUNTRY);
        album.setAlbumIcon(this.getClass().getResourceAsStream("/avatar.png"));
        album.setArtistId(1L);
        save();


    }

    @Test
    public void isBuildRight() throws SQLException, ConnectionManagerException, BuildException {
        EntityBuilder<Album> builder = new AlbumBuilder();
        Album testAlbum = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                testAlbum = builder.build(resultSet);
            }
            Assert.assertEquals(album.getName(), testAlbum.getName());
            Assert.assertEquals(album.getAlbumGenre(), testAlbum.getAlbumGenre());
            Assert.assertEquals(album.getAlbumState(), testAlbum.getAlbumState());
            Assert.assertEquals(album.getArtistId(), testAlbum.getArtistId());
            Assert.assertEquals(album.getReleaseDate(), testAlbum.getReleaseDate());
        }


    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }

}
