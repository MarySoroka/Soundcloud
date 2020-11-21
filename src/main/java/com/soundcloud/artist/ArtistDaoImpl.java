package com.soundcloud.artist;

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
 * Class, that implements ArtistDao interface and is used for database interaction
 */
@Bean(beanName = "artistDaoImpl")
public class ArtistDaoImpl implements ArtistDao {
    private static final Logger LOGGER = LogManager.getLogger(ArtistDaoImpl.class);
    private static final String SELECT_BY_USER_ID_QUERY = "select artist_id from soundcloud.artist where user_id = ?";
    private static final String SELECT_ALL_QUERY = "select artist_id, user_id from soundcloud.artist";
    private static final String SELECT_BY_ID_QUERY = "select artist_id, user_id from soundcloud.artist where artist_id = ?";
    private static final String SELECT_ARTIST_NAME_QUERY = "select user_account.user_login from soundcloud.artist join soundcloud.user_account on artist.user_id=user_account.user_id where artist.artist_id = ?";
    private static final String INSERT_QUERY = "insert into soundcloud.artist (user_id) values (?)";
    private static final String UPDATE_QUERY = "update soundcloud.artist set user_id=?  where artist_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud.artist where artist_id = ?";


    private final EntityBuilder<Artist> entityBuilder;
    private final ConnectionManager connectionManager;

    public ArtistDaoImpl(ConnectionManager connectionManager, @BeanQualifier(value = "artistBuilder") EntityBuilder<Artist> entityBuilder) {
        this.connectionManager = connectionManager;
        this.entityBuilder = entityBuilder;
    }


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


    @Override
    public boolean update(Long key, Artist album) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            setStatement(album, updateStatement, i);
            updateStatement.setLong(++i, album.getArtistId());
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
    public Long save(Artist artist) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            saveStatement.setLong(++i, artist.getUserId());
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }catch (Exception e) {
            LOGGER.error(DAO_SAVE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_SAVE_EXCEPTION.replace("0", ""), e);
        }

    }

    private void setStatement(Artist album, PreparedStatement statement, int i) throws SQLException {
        statement.setLong(++i, album.getUserId());
    }

    @Override
    public Optional<Artist> getById(Long id) throws DaoException {
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
    public List<Artist> getAll() throws DaoException {
        List<Artist> artists = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Artist> artist = build(resultSet);
                artist.ifPresent(artists::add);
            }
            return artists;
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
    public Artist clone(Artist album) {
        return new Artist(album);
    }

    @Override
    public Optional<Artist> build(ResultSet data) throws BuildException, DaoException {
        try {
            Artist artist = entityBuilder.build(data);
            return Optional.of(artist);
        } catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }
    @Override
    public Long getArtistIdByUserId(Long userId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, userId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }  catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }

    @Override
    public String getArtistName(Long artistId) throws DaoException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_ARTIST_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, artistId);
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoQueryExecuteException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }  catch (Exception e) {
            LOGGER.error(DAO_EXECUTE_EXCEPTION.replace("0", "{}"), e.getMessage());
            throw new DaoException(DAO_EXECUTE_EXCEPTION.replace("0", ""), e);
        }
    }
}
