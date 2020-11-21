package com.soundcloud.artist;

import com.soundcloud.TestUtils;
import com.soundcloud.album.AlbumBuilderTest;
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

import static com.soundcloud.TestUtils.createDB;
import static com.soundcloud.user.UserDaoTest.SAVE_EXCEPTION;

@RunWith(JUnit4.class)
public class ArtistBuilderTest {
    private static final String SELECT_ALL_QUERY = "select user_id,artist_id from soundcloud_test.artist";
    private static final String INSERT_QUERY = "insert into soundcloud_test.artist ( user_id, artist_id) values (?,?)";
    private static final Logger LOGGER = LogManager.getLogger(AlbumBuilderTest.class);
    private Artist artist;
    private ConnectionManager connectionManager;

    public void save() throws DaoQueryExecuteException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(artist, saveStatement, i);
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

    private void setStatement(Artist album, PreparedStatement statement, int i) throws SQLException {
        statement.setLong(++i, album.getUserId());
        statement.setLong(++i, album.getArtistId());
    }

    @Before
    public void init() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {

        ApplicationContext.initialize();
        connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();

        createDB(connection);
        artist = new Artist(1L,1L);
        save();


    }

    @Test
    public void isBuildRight() throws SQLException, ConnectionManagerException, BuildException {
        EntityBuilder<Artist> builder = new ArtistBuilder();
        Artist artist = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                artist = builder.build(resultSet);
            }
            Assert.assertEquals(this.artist.getArtistId(), artist.getArtistId());
            Assert.assertEquals(this.artist.getUserId(), artist.getUserId());
        }


    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }
}
