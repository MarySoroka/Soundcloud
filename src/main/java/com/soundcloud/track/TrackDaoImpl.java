package com.soundcloud.track;

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
import java.util.*;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "trackDaoImpl")
public class TrackDaoImpl implements TrackDao {
    private static final Logger LOGGER = LogManager.getLogger(TrackDaoImpl.class);
    private static final String SELECT_ALL_QUERY = "select track_id, track_name,plays_amount, album_id,track_path from soundcloud.track";
    private static final String SELECT_BY_ID_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud.track where track_id = ?";
    private static final String SELECT_BY_ALBUM_ID_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud.track where album_id = ?";
    private static final String SELECT_BY_NAME_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud.track where track_name = ? ";
    private static final String FIND_BY_NAME_QUERY = "select track_id, track_name,plays_amount, album_id, track_path from soundcloud.track where track_name COLLATE UTF8_GENERAL_CI  like ?";

    private static final String INSERT_QUERY = "insert into soundcloud.track ( track_name,plays_amount, album_id, track_path) values (?,?,?,?)";
    private static final String UPDATE_QUERY = "update soundcloud.track set track_name=?, plays_amount=?, album_id=?, track_path=?  where track_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud.track where album_id = ?";
    private static final String DELETE_TRACK_QUERY = "delete from soundcloud.track where track_id = ?";
    private final EntityBuilder<Track> entityBuilder;
    private final ConnectionManager connectionManager;

    public TrackDaoImpl(ConnectionManager connectionManager, @BeanQualifier(value = "trackBuilder") EntityBuilder<Track> entityBuilder) {
        this.connectionManager = connectionManager;
        this.entityBuilder = entityBuilder;
    }


    @Override
    public boolean delete(Long key) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_TRACK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
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


    @Override
    public boolean update(Long key, Track track) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            i = setStatement(track, updateStatement, i);
            updateStatement.setLong(++i, track.getId());
            return updateStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_UPDATE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_UPDATE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    @Override
    public Long save(Track track) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            setStatement(track, saveStatement, i);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            long trackId = generatedKeys.getLong(1);
            track.setId(trackId);
            return trackId;
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    private int setStatement(Track track, PreparedStatement statement, int i) throws SQLException {
        statement.setString(++i, track.getName());
        statement.setInt(++i, track.getPlaysAmount());
        statement.setLong(++i, track.getAlbumId());
        statement.setString(++i, track.getTrackPath());
        return i;
    }

    @Override
    public Optional<Track> getById(Long id) throws DaoException {
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

    @Override
    public List<Track> getAll() throws DaoException {
        List<Track> tracks = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            return tracks;
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

    @Override
    public Track clone(Track track) {
        return new Track(track);
    }

    @Override
    public Optional<Track> build(ResultSet data) throws BuildException, DaoException {
        try {
            Track track = entityBuilder.build(data);
            return Optional.of(track);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public Set<Track> getByAlbumId(Long albumId) throws DaoException {
        Set<Track> tracks = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_BY_ALBUM_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            getAllStatement.setLong(1, albumId);
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            return tracks;
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

    @Override
    public List<Track> getByName(String trackName) throws DaoException {
        List<Track> tracks = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            getAllStatement.setString(++i, trackName);
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            return tracks;
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

    @Override
    public List<Track> findByName(String trackName) throws DaoException {
        List<Track> tracks = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(FIND_BY_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            getAllStatement.setString(++i, "%" + trackName + "%");
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Track> track = build(resultSet);
                track.ifPresent(tracks::add);
            }
            return tracks;
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

    @Override
    public boolean deleteAllAlbumsTracks(Long albumId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            deleteStatement.setLong(1, albumId);
            return deleteStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        } catch (Exception e) {
            LOGGER.error(DAO_DELETE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_DELETE_EXCEPTION.replace("0", ""), e);
        }
    }
}
