package com.soundcloud.user;

import com.soundcloud.album.*;
import com.soundcloud.artist.*;
import com.soundcloud.connection.ConnectionManager;
import com.soundcloud.connection.ConnectionManagerImpl;
import com.soundcloud.dao.*;
import com.soundcloud.role.*;
import com.soundcloud.service.ServiceException;
import com.soundcloud.subscription.*;
import com.soundcloud.track.*;
import com.soundcloud.wallet.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.soundcloud.application.ApplicationConstants.*;

@RunWith(JUnit4.class)
public class UserServiceTest {

    public UserServiceTest() throws IOException {
        DataSource dataSource = DataSourceImpl.getInstance(DATABASE_PROPERTY_FILE);
        TransactionManager transactionManager = new TransactionManagerImpl(dataSource);
        ConnectionManager connectionManager = new ConnectionManagerImpl(transactionManager, dataSource);
        UserBuilder userBuilder = new UserBuilder();
        RoleBuilder roleBuilder = new RoleBuilder();
        AlbumBuilder albumBuilder = new AlbumBuilder();
        TrackBuilder trackBuilder = new TrackBuilder();
        WalletBuilder walletBuilder = new WalletBuilder();
        ArtistBuilder artistBuilder = new ArtistBuilder();

        RoleDao roleDao = Mockito.spy(new RoleDaoImpl(roleBuilder, connectionManager));
        RoleService roleService = Mockito.spy(new RoleServiceImpl(roleDao));
        this.roleService = roleService;

        WalletDao walletDao = Mockito.spy(new WalletDaoImpl(walletBuilder, connectionManager));
        WalletService walletService = Mockito.spy(new WalletServiceImpl(walletDao));
        this.walletService = walletService;

        UserDao userDao = Mockito.spy(new UserDaoImpl(connectionManager, userBuilder));
        this.userDao = userDao;

        AlbumDao albumDao = Mockito.spy(new AlbumDaoImpl(connectionManager, albumBuilder));

        ArtistDao artistDao = Mockito.spy(new ArtistDaoImpl(connectionManager, artistBuilder));
        ArtistService artistService = Mockito.spy(new ArtistServiceImpl(artistDao));
        this.artistService = artistService;

        TrackDao trackDao = Mockito.spy(new TrackDaoImpl(connectionManager, trackBuilder));
        TrackService trackService = Mockito.spy(new TrackServiceImpl(trackDao));
        this.trackService = trackService;

        AlbumService albumService = Mockito.spy(new AlbumServiceImpl(albumDao, trackService, artistService));
        this.albumService = albumService;
        SubscriptionBuilder subscriptionBuilder = new SubscriptionBuilder();

        SubscriptionDao subscriptionDao = Mockito.spy(new SubscriptionDaoImpl(connectionManager, subscriptionBuilder));
        subscriptionService = Mockito.spy(new SubscriptionServiceImpl(subscriptionDao));

        userService = Mockito.spy(new UserServiceImpl(roleService, walletService, userDao, albumService, artistService, trackService, subscriptionService));
        album = new Album("la", LocalDate.now(ZoneId.of("America/Montreal")), 0, AlbumState.ALBUM, AlbumGenre.COUNTRY);

    }

    private final Album album;
    private final UserDto dto = new UserDto(new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L), 1L);
    @Mock
    private final RoleService roleService;
    @Mock
    private final WalletService walletService;
    @Mock
    private final SubscriptionService subscriptionService;
    @Mock
    private final UserDao userDao;
    @Mock
    private final AlbumService albumService;
    @Mock
    private final ArtistService artistService;
    @Mock
    private final TrackService trackService;
    @Mock
    private final UserService userService;

    private final User user = new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L);


    @Test
    public void getAllArtistAlbums_isRight() throws ServiceException, IOException {
        album.setAlbumIcon(this.getClass().getResourceAsStream("/avatar.png"));
        album.setArtistId(1L);
        album.setId(1L);
        Mockito.doReturn(new LinkedList<Album>(Collections.singleton(album))).when(albumService).getAll();
        Mockito.doReturn(new HashSet<Track>()).when(trackService).getAlbumTracks(album.getId());
        Mockito.doReturn(new Artist(1L, 1L)).when(artistService).getUserByArtistId(album.getArtistId());

        Mockito.doReturn(new UserDto(user, 1L)).when(userService).getByIdUser(user.getId());
        Assert.assertTrue(userService.getAllArtistAlbums().size() > 0);

    }

    @Test
    public void updateUserInformation_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(userDao).update(user.getId(), user);
        Assert.assertTrue(userService.updateUserInformation(user));

    }


    @Test
    public void loginUser_isRight() throws ServiceException, SQLException, DaoException {
        Optional<User> userOptional = Optional.of(user);
        Mockito.doReturn(userOptional).when(userDao).findByLogin(user.getLogin());
        Subscription subscription = new Subscription(1L, LocalDate.now(), 1L, SubscriptionStatus.NOT_ACTIVE);
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionService).getSubscriptionByUserId(Mockito.anyLong());
        Optional<Wallet> wallet = Optional.of(new Wallet(1L, new BigDecimal(0)));
        Mockito.doReturn(wallet).when(walletService).getWalletByWalletId(userOptional.get().getWalletId());
        Mockito.doReturn(new HashSet<Role>()).when(roleService).getUserRoles(userOptional.get().getId());
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(user.getId());
        Assert.assertNotNull(userService.loginUser(user.getLogin(), user.getPassword()));
    }

    @Test
    public void registerUser_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(1L).when(walletService).saveDefaultWallet();
        Mockito.doReturn(1L).when(userDao).save(user);
        Mockito.doReturn(1L).when(artistService).saveArtist(new Artist(1L));
        Mockito.doReturn(1L).when(roleService).assignRole(1L);

        Assert.assertTrue(userService.registerUser(user));

    }

    @Test
    public void followUser_isRight() throws DaoException, ServiceException {
        Mockito.doReturn(true).when(userDao).followUser(1L, 1L, 1L, 1L);
        Assert.assertTrue(userService.followUser(1L, 1L, 1L, 1L));

    }

    @Test
    public void unfollowUser_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(true).when(userDao).unfollowUser(1L, 1, 1L, 1);
        Assert.assertTrue(userService.unfollowUser(1L, 1, 1L, 1));
    }

    @Test
    public void deleteUser_isRight() throws IOException, ServiceException, DaoException {
        Mockito.doReturn(new UserDto(user, 1L)).when(userService).getByIdUser(user.getId());
        Mockito.doReturn(new LinkedList<User>(Collections.singleton(user))).when(userDao).getFollowUsers(user.getId());
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumService).getLikedAlbums(user.getId());
        Mockito.doReturn(true).when(albumService).deleteLikedAlbum(album.getId(), user.getId(), album.getLikesAmount());
        Mockito.doReturn(true).when(userService).unfollowUser(user.getId(), user.getUserFollows(), user.getId(), user.getUserFollowers());
        Mockito.doReturn(true).when(userDao).delete(user.getId());

        Assert.assertTrue(userService.deleteUser(user.getId()));

    }

    @Test
    public void getUserByArtistId_isRight() throws ServiceException {
        Mockito.doReturn(new Artist(1L, 1L)).when(artistService).getUserByArtistId(1L);
        Long userByArtistId = userService.getUserByArtistId(1L);
        Assert.assertEquals(user.getId(), userByArtistId);
    }

    @Test(expected = ServiceException.class)
    public void getUserProfile_isRight() throws ServiceException, DaoException, IOException {
        album.setArtistId(1L);
        user.setUserIcon(this.getClass().getResourceAsStream("/avatar.png"));
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumService).getLikedAlbums(user.getId());
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumService).getArtistAlbums(user.getId());
        Mockito.doReturn(Optional.empty()).when(userDao).getById(1L);
        LinkedList<UserDto> users = new LinkedList<>(Collections.singleton(new UserDto(this.user, 1L)));
        Mockito.doReturn(users).when(userService).getFollowUsers(this.user.getId());
        Mockito.doReturn(users).when(userService).getFollowerUsers(1L);
        Mockito.doReturn(1L).when(userService).getUserByArtistId(this.user.getId());
        Mockito.doReturn(new Artist(1L, 1L)).when(artistService).getUserByArtistId(this.user.getId());
        Mockito.doReturn(Optional.empty()).when(userDao).getById(1L);
        Mockito.doReturn(dto).when(userService).getByIdUser(1L);
        userService.getUserProfile(1L, 1L);
    }


    @Test
    public void getAllUsers_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(user))).when(userDao).getAll();
        Assert.assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    public void getByIdUser_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new HashSet<>()).when(roleService).getUserRoles(user.getId());
        Mockito.doReturn(Optional.of(user)).when(userDao).getById(user.getId());
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(Mockito.anyLong());
        Subscription subscription = new Subscription(1L, LocalDate.now(), 1L, SubscriptionStatus.NOT_ACTIVE);
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionService).getSubscriptionByUserId(Mockito.anyLong());
        Assert.assertEquals(user.getId(), userService.getByIdUser(user.getId()).getId());
    }

    @Test
    public void getUser_isRight() throws DaoException, ServiceException {
        Mockito.doReturn(Optional.of(user)).when(userDao).getById(user.getId());
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(Mockito.anyLong());
        Assert.assertEquals(user.getId(), userService.getUser(user.getId()).getId());

    }

    @Test(expected = ServiceException.class)
    public void getUserLibrary_isRight() throws ServiceException, DaoException {
        album.setArtistId(1L);
        Mockito.doReturn(Optional.empty()).when(userDao).getById(user.getId());

        userService.getUserLibrary(user.getId());
    }

    @Test
    public void getUserLikedAlbums_isRight() throws ServiceException {
        album.setArtistId(1L);
        Mockito.doReturn(new LinkedList<>(Collections.singleton(album))).when(albumService).getLikedAlbums(user.getId());
        Mockito.doReturn(new Artist(1L, 1L)).when(artistService).getUserByArtistId(user.getId());
        Mockito.doReturn(dto).when(userService).getByIdUser(1L);
        Assert.assertEquals(album.getId(), userService.getUserLikedAlbums(user.getId()).getId());

    }

    @Test
    public void getFollowerUsers_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(user))).when(userDao).getFollowerUsers(user.getId());
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(user.getId());
        Assert.assertEquals(1, userService.getFollowerUsers(dto.getId()).size());
    }

    @Test
    public void getFollowUsers_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(user))).when(userDao).getFollowUsers(user.getId());
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(user.getId());
        Assert.assertEquals(1, userService.getFollowUsers(user.getId()).size());
    }

    @Test
    public void getNotFollowUsers_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(new LinkedList<>(Collections.singleton(user))).when(userDao).getNotFollowUsers(user.getId());
        Mockito.doReturn(new HashSet<>()).when(roleService).getUserRoles(Mockito.anyLong());
        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(user.getId());
        Assert.assertEquals(1, userService.getNotFollowUsers(user.getId()).size());
    }

    @Test
    public void subscribeUser_isRight() throws ServiceException {
        Mockito.doReturn(dto).when(userService).getByIdUser(Mockito.anyLong());
        Wallet wallet = new Wallet(1L, new BigDecimal(10));
        Mockito.doReturn(Optional.of(wallet)).when(walletService).getWalletByWalletId(Mockito.anyLong());
        Mockito.doReturn(true).when(walletService).setWalletAmount(Mockito.anyLong(), Mockito.any(Wallet.class));
        Subscription subscription = new Subscription(1L, LocalDate.now(), 1L, SubscriptionStatus.NOT_ACTIVE);
        Mockito.doReturn(1L).when(subscriptionService).saveSubscription(subscription);
        Mockito.doReturn(new HashSet<>()).when(roleService).getUserRoles(Mockito.anyLong());
        Mockito.doNothing().when(roleService).updateUserRoles(Mockito.anyLong(), Mockito.anyList());

        Assert.assertTrue(userService.subscribeUser(1L, wallet.getAmount()));
    }

    @Test
    public void updateUserRoles_isRight() throws ServiceException {
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(new Role(1L, 1L));
        Mockito.doReturn(userRoles).when(roleService).getUserRoles(Mockito.anyLong());
        Mockito.doReturn(1L).when(roleService).assignRole(Mockito.any(Role.class));
        List<Role> userRolesTest = new LinkedList<>();
        userRolesTest.add(new Role(1L, 1L));
        userRolesTest.add(new Role(1L, 2L));

        userService.updateUserRoles(1L, userRolesTest);
        Mockito.verify(roleService).assignRole(Mockito.any(Role.class));
    }

    @Test
    public void getUserWalletAmount_isRight() throws ServiceException, DaoException {
        Mockito.doReturn(dto).when(userService).getByIdUser(Mockito.anyLong());
        Wallet wallet = new Wallet(1L, new BigDecimal(10));
        Mockito.doReturn(Optional.of(wallet)).when(walletService).getWalletByWalletId(Mockito.anyLong());

        Assert.assertEquals(wallet.getAmount(), userService.getUserWalletAmount(1L));

    }

    @Test
    public void addUserWallet_isRight() throws ServiceException {
        Mockito.doReturn(dto).when(userService).getByIdUser(Mockito.anyLong());
        Mockito.doReturn(true).when(walletService).setWalletAmount(Mockito.anyLong(), Mockito.any(Wallet.class));
        userService.addUserWallet(1L, new BigDecimal(10));
        Mockito.verify(walletService).setWalletAmount(Mockito.anyLong(), Mockito.any(Wallet.class));
    }

    @Test
    public void subscribed_isRight() throws ServiceException, DaoException {
        Subscription subscription = new Subscription(1L, LocalDate.now(), 1L, SubscriptionStatus.NOT_ACTIVE);

        Mockito.doReturn(Optional.of(subscription)).when(subscriptionService).getSubscriptionByUserId(Mockito.anyLong());
        Mockito.doReturn(Optional.of(user)).when(userDao).getById(Mockito.anyLong());
        Wallet wallet = new Wallet(1L, new BigDecimal(10));
        Mockito.doReturn(Optional.of(wallet)).when(walletService).getWalletByWalletId(Mockito.anyLong());
        Mockito.doReturn(true).when(walletService).setWalletAmount(Mockito.anyLong(), Mockito.any(Wallet.class));
        Mockito.doReturn(true).when(subscriptionService).updateSubscription(Mockito.any(Subscription.class));
        boolean subscribed = userService.subscribed(dto);
        Assert.assertTrue(subscribed);

    }

    @Test
    public void unsubscribed_isRight() throws ServiceException {
        Subscription subscription = new Subscription(1L, LocalDate.now(), 1L, SubscriptionStatus.NOT_ACTIVE);
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionService).getSubscriptionByUserId(Mockito.anyLong());
        Mockito.doReturn(true).when(subscriptionService).deleteSubscription(Mockito.anyLong());
        Assert.assertTrue(userService.unsubscribed(1L));


    }

    @Test
    public void findUsersAlbumsByName() throws ServiceException, SQLException, DaoException {

        Mockito.doReturn(Optional.of(user)).when(userDao).findByName(Mockito.anyString());


        Mockito.doReturn(new LinkedList<>()).when(albumService).findAlbumByName(Mockito.anyString());
        Mockito.doReturn(new LinkedList<>()).when(trackService).findTrackByName(Mockito.anyString());

        Mockito.doReturn(1L).when(artistService).getArtistIdByUserId(Mockito.anyLong());
        Mockito.doReturn(new LinkedList<>()).when(albumService).getArtistAlbums(Mockito.anyLong());

       userService.findUsersAlbumsByName("data");
       Mockito.verify(artistService).getArtistIdByUserId(Mockito.anyLong());
       Mockito.verify(albumService).getArtistAlbums(Mockito.anyLong());

    }


}