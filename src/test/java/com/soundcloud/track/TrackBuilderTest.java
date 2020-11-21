package com.soundcloud.track;

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
public class TrackBuilderTest {
    private static final String SELECT_ALL_QUERY = "select track_id, track_name,plays_amount, album_id,track_path from soundcloud_test.track";
    private static final String INSERT_QUERY = "insert into soundcloud_test.track ( track_name,plays_amount, album_id, track_path) values (?,?,?,?)";
    private static final Logger LOGGER = LogManager.getLogger(AlbumBuilderTest.class);
    private Track track;
    private ConnectionManager connectionManager;

    public void save() throws DaoQueryExecuteException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(track, saveStatement, i);
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

    private void setStatement(Track track, PreparedStatement statement, int i) throws SQLException {
        statement.setString(++i, track.getName());
        statement.setInt(++i, track.getPlaysAmount());
        statement.setLong(++i, track.getAlbumId());
        statement.setString(++i, track.getTrackPath());
    }

    @Before
    public void init() throws SQLException, DaoQueryExecuteException, ConnectionManagerException {

        ApplicationContext.initialize();
        connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();

        createDB(connection);
        track = new Track(0L,"name",0, 1L,"");
        save();


    }

    @Test
    public void isBuildRight() throws SQLException, ConnectionManagerException, BuildException {
        EntityBuilder<Track> builder = new TrackBuilder();
        Track track = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                track = builder.build(resultSet);
            }
            Assert.assertEquals(this.track.getId(), track.getId());
            Assert.assertEquals(this.track.getName(), track.getName());
            Assert.assertEquals(this.track.getPlaysAmount(), track.getPlaysAmount());
            Assert.assertEquals(this.track.getTrackPath(), track.getTrackPath());
        }


    }


    @After
    public void dropDB() throws SQLException, ConnectionManagerException {
        TestUtils.dropDB(connectionManager.getConnection());
    }
}
