package com.soundcloud.artist;

import com.soundcloud.application.ApplicationContext;
import com.soundcloud.builder.BuildException;
import com.soundcloud.builder.EntityBuilder;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerException;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.DaoQueryExecuteException;
import com.soundcloud.user.User;
import com.soundcloud.user.UserDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.soundcloud.TestUtils.createDB;

@RunWith(JUnit4.class)
public class ArtistDaoTest {
    static {
        ApplicationContext.initialize();
    }

    private final ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
    ;

    private Artist artist;
    private static final String INSERT_USER_QUERY = "insert into soundcloud_test.user_account ( user_login, user_password, user_email,user_follows,user_followers, wallet_id, USER_ICON) values (?,?,?,?,?,?,?)";

    private static final String SELECT_BY_USER_ID_QUERY = "select artist_id from soundcloud_test.artist where user_id = ?";
    private static final String SELECT_ALL_QUERY = "select artist_id, user_id from soundcloud_test.artist";
    private static final String SELECT_BY_ID_QUERY = "select artist_id, user_id from soundcloud_test.artist where artist_id = ?";
    private static final String SELECT_ARTIST_NAME_QUERY = "select user_account.user_login from soundcloud_test.artist join soundcloud_test.user_account on artist.user_id=user_account.user_id where artist.artist_id = ?";
    private static final String INSERT_QUERY = "insert into soundcloud_test.artist (user_id) values (?)";
    private static final String UPDATE_QUERY = "update soundcloud_test.artist set user_id=?  where artist_id = ?";
    private static final String DELETE_QUERY = "delete from soundcloud_test.artist where artist_id = ?";

    @BeforeClass
    public static void init() throws SQLException, ConnectionManagerException {
        ConnectionManager connectionManager = ApplicationContext.getInstance().getBean(ConnectionManagerImpl.class);
        Connection connection = connectionManager.getConnection();
        createDB(connection);


    }

    @Before
    public void initArtist() throws ConnectionManagerException, SQLException {
        artist = new Artist(1L, 0L);
        save();
    }

    @Test
    public void update() throws ConnectionManagerException, SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            i = setStatement(artist, updateStatement, i);
            updateStatement.setLong(++i, artist.getArtistId());
            Assert.assertTrue(updateStatement.executeUpdate() > 0);
        }

    }

    @Test
    public void delete() throws SQLException, ConnectionManagerException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
            deleteStatement.setLong(1, artist.getArtistId());
            Assert.assertTrue(deleteStatement.executeUpdate() > 0);
        }
    }


    public void save() throws ConnectionManagerException, SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            saveStatement.setLong(++i, artist.getUserId());
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            Assert.assertTrue(generatedKeys.getLong(1) >= 0);
        }

    }

    private int setStatement(Artist album, PreparedStatement statement, int i) throws SQLException {
        statement.setLong(++i, album.getUserId());
        return i;
    }

    @Test
    public void getById() throws DaoException, ConnectionManagerException, SQLException, BuildException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, artist.getArtistId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            Assert.assertNotNull(build(resultSet));
        }
    }

    @Test
    public void getAll() throws DaoException, ConnectionManagerException, SQLException, BuildException {
        save();
        List<Artist> artists = new LinkedList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement getAllStatement = connection.prepareStatement(SELECT_ALL_QUERY, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = getAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Optional<Artist> artist = build(resultSet);
                artist.ifPresent(artists::add);
            }
            Assert.assertFalse(artists.isEmpty());
        }
    }


    public Optional<Artist> build(ResultSet data) throws BuildException, DaoException, SQLException {
        EntityBuilder<Artist> entityBuilder = new ArtistBuilder();
        Artist artist = entityBuilder.build(data);
        return Optional.of(artist);

    }

    @Test
    public void getArtistIdByUserId() throws DaoException, ConnectionManagerException, SQLException {
        save();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, artist.getUserId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            resultSet.next();
            Assert.assertTrue(resultSet.getLong(1) >= 0);
        }
    }

    @Test
    public void getArtistName() throws ConnectionManagerException, SQLException, DaoQueryExecuteException, IOException {
        save();
        saveUser();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(SELECT_ARTIST_NAME_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            findByIdStatement.setLong(1, artist.getArtistId());
            ResultSet resultSet = findByIdStatement.executeQuery();
            Assert.assertTrue(resultSet.next());
        }
    }

    private Long saveUser() throws DaoQueryExecuteException, ConnectionManagerException, SQLException, IOException {
        User user = new User("flash",
                "123",
                "m@gmail.com", 0, 0);
        user.setId(1L);
        UserDto userDTO = new UserDto(user, new HashSet<>());
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveStatement = connection.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            setUserStatement(userDTO, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            generatedKeys.next();
            Long id = generatedKeys.getLong(1);
            return id;
        }
    }

    private int setUserStatement(UserDto userDTO, PreparedStatement statement) throws SQLException {
        int i = 0;
        statement.setString(++i, userDTO.getLogin());
        statement.setString(++i, userDTO.getPassword());
        statement.setString(++i, userDTO.getEmail());
        statement.setInt(++i, userDTO.getUserFollowers());
        statement.setInt(++i, userDTO.getUserFollows());
        statement.setLong(++i, 0L);
        statement.setBlob(++i, this.getClass().getResourceAsStream("/soundcloud.png"));
        return i;
    }
}
