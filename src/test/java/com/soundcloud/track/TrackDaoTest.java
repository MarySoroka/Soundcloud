package com.soundcloud.track;

import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DaoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.*;

import static com.soundcloud.TestUtils.createDB;

public class TrackDaoTest {
    private static final String SELECT_ALL_QUERY = "select track_id, track_name,plays_amount, album_id,track_path from soundcloud_test.track";
    private static final String SELECT_BY_ID_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud_test.track where track_id = ?";
    private static final String SELECT_BY_ALBUM_ID_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud_test.track where album_id = ?";
    private static final String SELECT_BY_NAME_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud_test.track where track_name = ? ";
    private static final String INSERT_QUERY = "insert into soundcloud_test.track ( track_name,plays_amount, album_id, track_path) values (?,?,?,?)";
    private static final String UPDATE_QUERY = "update soundcloud_test.track set track_name=?, plays_amount=?, album_id=?, track_path=?  where track_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud_test.track where album_id = ?";
    private static final String DELETE_TRACK_QUERY = "delete from soundcloud_test.track where track_id = ?";
    private Track track;

    @BeforeClass
    public static void init() throws SQLException, ConnectionManagerException {
        ApplicationContext.initialize();
        ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();
        createDB(connection);

    }

    private final ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);

    @Before
    public void initAlbum() throws SQLException, ConnectionManagerException, DaoException {
        track = new Track(0L, "name", 0, 1L, "");
        save();
    }


    @Test
    public void delete() throws DaoException, ConnectionManagerException, SQLException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_TRACK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1, track.getId());
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }


    @Test
    public void update() throws DaoException, ConnectionManagerException, SQLException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            i= setStatement(track, updateStatement, i);
            updateStatement.setLong(++i, track.getId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }

    }

    public void save() throws DaoException, ConnectionManagerException, SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(track, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            long trackId = generatedKeys.getLong(1);
            track.setId(trackId);
        }

    }

    private int setStatement(Track track, PreparedStatement statement, int i) throws SQLException {
        statement.setString(++i, track.getName());
        statement.setInt(++i, track.getPlaysAmount());
        statement.setLong(++i, track.getAlbumId());
        statement.setString(++i, track.getTrackPath());
        return  i;
    }

    @Test
    public void getById() throws DaoException, ConnectionManagerException, SQLException, BuildException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, track.getId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            Assert.assertTrue(build(resultSet).isPresent());
        }
    }

    @Test
    public void getAll() throws DaoException, ConnectionManagerException, SQLException, BuildException {
        save();
        List<Track> tracks = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            Assert.assertFalse(tracks.isEmpty());
        }
    }


    public Optional<Track> build(ResultSet data) throws BuildException, DaoException, SQLException {
        EntityBuilder<Track> entityBuilder = new TrackBuilder();
        Track track = entityBuilder.build(data);
        return Optional.of(track);

    }

    @Test
    public void getByAlbumId() throws DaoException, ConnectionManagerException, SQLException, BuildException {
        save();
        Set<Track> tracks = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_BY_ALBUM_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            getAllStatement.setLong(1, track.getAlbumId());
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            Assert.assertFalse(tracks.isEmpty());

        }
    }

    @Test
    public void getByName() throws DaoException, ConnectionManagerException, SQLException, BuildException {
        save();
        List<Track> tracks = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            getAllStatement.setString(++i, track.getName());
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            Assert.assertFalse(tracks.isEmpty());
        }
    }

    @Test
    public void deleteAllAlbumsTracks() throws ConnectionManagerException, SQLException, DaoException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1, track.getAlbumId());
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }
}
