package com.soundcloud.user;

import com.soundcloud.album.Album;
import com.soundcloud.album.AlbumService;
import com.soundcloud.artist.Artist;
import com.soundcloud.artist.ArtistService;
import com.soundcloud.bean.Bean;
import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.TransactionSupport;
import com.soundcloud.dao.Transactional;
import com.soundcloud.role.Role;
import com.soundcloud.role.RoleService;
import com.soundcloud.role.RoleType;
import com.soundcloud.service.ServiceException;
import com.soundcloud.subscription.Subscription;
import com.soundcloud.subscription.SubscriptionService;
import com.soundcloud.subscription.SubscriptionStatus;
import com.soundcloud.track.Track;
import com.soundcloud.track.TrackService;
import com.soundcloud.wallet.Wallet;
import com.soundcloud.wallet.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static com.soundcloud.application.ApplicationConstants.*;

@TransactionSupport
@Bean(beanName = "UserService")
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private final RoleService roleService;
    private final WalletService walletService;
    private final SubscriptionService subscriptionService;
    private final UserDao userDao;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final TrackService trackService;

    public UserServiceImpl(RoleService roleService,
                           WalletService walletService,
                           UserDao userDao,
                           AlbumService albumService,
                           ArtistService artistService,
                           TrackService trackService,
                           SubscriptionService subscriptionService) {
        this.roleService = roleService;
        this.walletService = walletService;
        this.userDao = userDao;
        this.albumService = albumService;
        this.artistService = artistService;
        this.trackService = trackService;
        this.subscriptionService = subscriptionService;
    }


    @Override
    public List<UserDto> getAllArtistAlbums(Long userId) throws ServiceException {
        try {
            List<Album> albums = albumService.getAll();
            Set<Artist> artists = new HashSet<>();
            for (Album album :
                    albums) {
                Set<Track> albumTracks = trackService.getAlbumTracks(album.getId());
                album.setTrackList(albumTracks);
                Artist artist = artistService.getUserByArtistId(album.getArtistId());
                artists.add(artist);
            }
            List<UserDto> users = new LinkedList<>();
            for (Artist a :
                    artists) {
                List<Album> albumSet = new LinkedList<>();
                for (Album album :
                        albums) {
                    if (a.getArtistId().equals(album.getArtistId()) && !a.getUserId().equals(userId)) {
                        albumSet.add(album);
                    }
                }
                UserDto user = getByIdUser(a.getUserId());
                user.setUserAlbums(albumSet);
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<UserDto> getAllArtistAlbums() throws ServiceException {
        try {
            List<Album> albums = albumService.getAll();
            Set<Artist> artists = new HashSet<>();
            for (Album album :
                    albums) {
                Set<Track> albumTracks = trackService.getAlbumTracks(album.getId());
                album.setTrackList(albumTracks);
                Artist artist = artistService.getUserByArtistId(album.getArtistId());
                artists.add(artist);
            }
            List<UserDto> users = new LinkedList<>();
            for (Artist a :
                    artists) {
                List<Album> albumSet = new LinkedList<>();
                for (Album album :
                        albums) {
                    if (a.getArtistId().equals(album.getArtistId())) {
                        albumSet.add(album);
                    }
                }
                UserDto user = getByIdUser(a.getUserId());
                user.setUserAlbums(albumSet);
                user.setArtistId(a.getArtistId());
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean updateUserInformation(User user) throws ServiceException {
        try {
            return userDao.update(user.getId(), user);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean subscribeUser(Long userId, BigDecimal walletAmount) throws ServiceException {
        UserDto user = getByIdUser(userId);
        Optional<Wallet> wallet = walletService.getWalletByWalletId(user.getWalletId());
        if (wallet.isPresent()) {
            BigDecimal currentWalletAmount = wallet.get().getAmount();
            BigDecimal balance = currentWalletAmount.subtract(walletAmount);
            if (balance.compareTo(BigDecimal.ZERO) >= 0) {
                wallet.get().setAmount(balance);
                walletService.setWalletAmount(userId, wallet.get());
                Subscription subscription = new Subscription(LocalDate.now(), userId);
                Long subscriptionId = subscriptionService.saveSubscription(subscription);
                subscription.setSubscriptionId(subscriptionId);
                Set<Role> userRoles = roleService.getUserRoles(userId);
                userRoles.add(new Role(RoleType.SINGED_USER, RoleType.SINGED_USER.getRoleTypeId(), userId));
                List<Role> updateRoles = new LinkedList<>(userRoles);
                roleService.updateUserRoles(userId, updateRoles);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateUserRoles(Long userId, List<Role> userRoles) throws ServiceException {
        Set<Role> roles = roleService.getUserRoles(userId);
        roles.forEach(role -> userRoles.removeIf(r -> r.equals(role)));
        for (Role userRole : userRoles) {
            roleService.assignRole(userRole);
        }

    }

    @Override
    public BigDecimal getUserWalletAmount(Long userId) throws ServiceException {
        UserDto user = getByIdUser(userId);
        Optional<Wallet> wallet = walletService.getWalletByWalletId(user.getWalletId());
        if (wallet.isPresent()) {
            return wallet.get().getAmount();
        } else {
            throw new ServiceException();
        }

    }

    @Override
    public void addUserWallet(Long userId, BigDecimal walletAmount) throws ServiceException {
        UserDto user = getByIdUser(userId);
        walletService.setWalletAmount(userId, new Wallet(user.getWalletId(), walletAmount));
    }

    @Override
    public boolean subscribed(UserDto userDto) throws ServiceException {
        try {
            Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(userDto.getId());
            if (subscription.isPresent()) {
                boolean isSubscribe = subscription.get().getSubscriptionDate().isBefore(LocalDate.now()) || subscription.get().getSubscriptionDate().isEqual(LocalDate.now());
                if (isSubscribe) {
                    Optional<User> user = userDao.getById(userDto.getId());
                    if (user.isPresent()) {
                        Optional<Wallet> userWallet = walletService.getWalletByWalletId(user.get().getWalletId());
                        if (userWallet.isPresent() && userWallet.get().getAmount().compareTo(SUBSCRIBE_AMOUNT) >= 0) {
                            BigDecimal balance = userWallet.get().getAmount().subtract(SUBSCRIBE_AMOUNT);
                            userWallet.get().setAmount(balance);
                            walletService.setWalletAmount(userWallet.get().getWalletId(), userWallet.get());
                            LocalDate subscriptionDate = subscription.get().getSubscriptionDate().plusMonths(1L);
                            subscription.get().setSubscriptionDate(subscriptionDate);
                            subscription.get().setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                            userDto.setSubscription(subscription.get());
                            subscriptionService.updateSubscription(subscription.get());
                            return true;
                        }
                    }
                    subscription.get().setSubscriptionStatus(SubscriptionStatus.NOT_ACTIVE);
                    subscriptionService.updateSubscription(subscription.get());
                    return false;
                }
                return true;
            }
            return false;
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean unsubscribed(Long userId) throws ServiceException {
        Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(userId);
        if (subscription.isPresent()) {
            return subscriptionService.deleteSubscription(subscription.get().getSubscriptionId());
        }
        return false;

    }

    @Override
    public List<UserDto> findUsersAlbumsByName(String searchData) throws ServiceException {
        try {
            Optional<User> user = userDao.findByName(searchData);
            List<UserDto> users = new LinkedList<>();

            List<Album> albums = albumService.findAlbumByName(searchData);
            List<Track> tracks = trackService.findTrackByName(searchData);
            for (Album album : albums) {
                Artist artistAlbum = artistService.getUserByArtistId(album.getArtistId());
                UserDto userById = getByIdUser(artistAlbum.getUserId());
                album.setArtistName(userById.getLogin());
                userById.setUserAlbums(new LinkedList<>(Collections.singleton(album)));
                users.add(userById);
            }

            for (Track track : tracks) {
                List<Album> trackAlbums = new LinkedList<>();
                Album trackAlbum = albumService.getAlbumById(track.getAlbumId());
                UserDto albumUser = getByIdUser(artistService.getUserByArtistId(trackAlbum.getArtistId()).getUserId());
                HashSet<Track> trackList = new HashSet<>();
                trackList.add(track);
                trackAlbum.setTrackList(trackList);
                trackAlbums.add(trackAlbum);
                albumUser.setUserAlbums(trackAlbums);
                users.add(albumUser);
            }
            if (user.isPresent()) {
                Long artistId = artistService.getArtistIdByUserId(user.get().getId());
                List<Album> albumList = albumService.getArtistAlbums(artistId);
                UserDto userDto = new UserDto(user.get(), user.get().getId());
                userDto.setUserAlbums(albumList);
                users.add(userDto);
            }
            if (users.isEmpty()) {
                return Collections.emptyList();
            }
            return users;
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }

    }


    @Override
    public Optional<UserDto> loginUser(String login, String password) throws ServiceException {
        try {
            Optional<User> user = userDao.findByLogin(login);
            Optional<User> userByLogin = user.filter(u -> password.equals(user.get().getPassword()));
            if (userByLogin.isPresent()) {
                Optional<Wallet> wallet = walletService.getWalletByWalletId(user.get().getWalletId());
                if (wallet.isPresent()) {
                    Set<Role> userRoles = roleService.getUserRoles(user.get().getId());
                    User loginUser = userByLogin.get();
                    Long artistId = artistService.getArtistIdByUserId(loginUser.getId());
                    loginUser.setArtistId(artistId);
                    UserDto userDto = new UserDto(user.get(), userRoles);
                    Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(userDto.getId());
                    subscription.ifPresent(subscription1 -> userDto.setSubscription(subscription.get()));
                    return Optional.of(userDto);
                }
            }
            return Optional.empty();
        } catch (DaoException | SQLException | IOException e) {
            LOGGER.error(COMMAND_LOGIN_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()));
        }
    }

    @Transactional
    @Override
    public boolean registerUser(User user) throws ServiceException {
        try {
            Optional<User> userByLogin = userDao.findByLogin(user.getLogin());
            if (userByLogin.isPresent()){
                throw new ServiceException(COMMAND_USER_PRESENTS);
            }
            Long walletId = walletService.saveDefaultWallet();
            user.setWalletId(walletId);
            Long userId = userDao.save(user);
            Long artistId = artistService.saveArtist(new Artist(userId));
            user.setArtistId(artistId);
            roleService.assignRole(userId);
            return true;
        } catch (ServiceException | DaoException | SQLException e) {
            LOGGER.error(COMMAND_REGISTER_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(COMMAND_REGISTER_EXCEPTION.replace("0", e.getMessage()));
        }
    }

    @Override
    public boolean followUser(Long userId, Long followsAmount, Long followUserId, Long followersAmount) throws
            ServiceException {
        try {
            return userDao.followUser(userId, followsAmount, followUserId, followersAmount);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean unfollowUser(Long userId, Integer followsAmount, Long followUserId, Integer followersAmount) throws
            ServiceException {
        try {
            return userDao.unfollowUser(userId, followsAmount, followUserId, followersAmount);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<UserDto> getAdminUsers() throws ServiceException {
        List<UserDto> usersDto = new LinkedList<>();
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                Set<Role> userRoles = roleService.getUserRoles(user.getId());
                boolean isAdmin = userRoles.stream().anyMatch(role -> role.getName().equals(RoleType.ADMIN));
                if (!isAdmin) {
                    Long artistId = artistService.getArtistIdByUserId(user.getId());
                    user.setArtistId(artistId);
                    List<Album> artistAlbums = albumService.getArtistAlbums(artistId);
                    UserDto userDto = new UserDto(user, userRoles);
                    userDto.setUserAlbums(artistAlbums);
                    Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(user.getId());
                    subscription.ifPresent(subscription1 -> userDto.setSubscription(subscription.get()));
                    usersDto.add(userDto);
                }
            }
            return usersDto;
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteUser(Long userId) throws ServiceException {
        try {
            UserDto byIdUser = getByIdUser(userId);
            List<User> followUsers = userDao.getFollowUsers(userId);
            List<Album> likedAlbums = albumService.getLikedAlbums(userId);
            for (Album a : likedAlbums) {
                albumService.deleteLikedAlbum(a.getId(), userId, a.getLikesAmount());
            }
            for (User u : followUsers) {
                unfollowUser(userId, byIdUser.getUserFollows(), u.getId(), u.getUserFollowers());
            }
            return userDao.delete(userId);
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public Long getUserByArtistId(Long artistId) throws ServiceException {
        Artist userByArtistId = artistService.getUserByArtistId(artistId);
        return userByArtistId.getUserId();
    }

    @Override
    public UserDto getUserProfile(Long userId, Long artistId) throws ServiceException {
        try {
            List<Album> userAlbums = albumService.getArtistAlbums(artistId);
            List<Album> likedAlbums = albumService.getLikedAlbums(userId);
            Optional<User> userDaoById = userDao.getById(userId);
            List<UserDto> followUsers = getFollowUsers(userId);
            List<UserDto> followerUsers = getFollowerUsers(userId);
            if (userDaoById.isPresent()) {
                User user = userDaoById.get();
                for (Album a : likedAlbums) {
                    Long userByArtistId = getUserByArtistId(a.getArtistId());
                    UserDto byIdUser = getByIdUser(userByArtistId);
                    a.setArtistName(byIdUser.getLogin());
                }
                UserDto userDto = new UserDto(user, userAlbums, likedAlbums);
                userDto.setFollowsUser(followUsers);
                userDto.setFollowersUser(followerUsers);
                userDto.setArtistId(artistId);
                Set<Role> userRoles = roleService.getUserRoles(userId);
                userDto.setUserRoles(userRoles);
                Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(userId);
                subscription.ifPresent(subscription1 -> userDto.setSubscription(subscription.get()));
                return userDto;
            } else {
                throw new ServiceException(SERVICE_FIND_EXCEPTION);
            }

        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }


    @Override
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDao.getAll();
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public UserDto getByIdUser(Long userId) throws ServiceException {
        try {
            Set<Role> userRoles = roleService.getUserRoles(userId);
            Optional<User> user = userDao.getById(userId);
            Long artistId = artistService.getArtistIdByUserId(userId);
            if (user.isPresent()) {
                UserDto userDto = new UserDto(user.get(), userRoles);
                userDto.setArtistId(artistId);
                Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(userId);
                subscription.ifPresent(subscription1 -> userDto.setSubscription(subscription.get()));
                return userDto;
            } else {
                throw new ServiceException(SERVICE_EXCEPTION);
            }
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public User getUser(Long userId) throws ServiceException {
        try {
            Optional<User> user = userDao.getById(userId);
            if (user.isPresent()) {
                Long artistId = artistService.getArtistIdByUserId(user.get().getId());
                user.get().setArtistId(artistId);
                return user.get();
            } else {
                throw new ServiceException(SERVICE_EXCEPTION);
            }
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public UserDto getUserLibrary(Long userId) throws ServiceException {
        try {
            Optional<User> user = userDao.getById(userId);
            if (user.isPresent()) {
                Long artistId = artistService.getArtistIdByUserId(userId);
                List<Album> artistAlbums = albumService.getArtistAlbums(artistId);
                List<Album> likedAlbums = albumService.getLikedAlbums(userId);
                List<UserDto> followerUsers = getFollowerUsers(userId);
                List<UserDto> followUsers = getFollowUsers(userId);
                for (Album album :
                        likedAlbums) {
                    Artist userByArtistId = artistService.getUserByArtistId(album.getArtistId());
                    Optional<User> optionalUser = userDao.getById(userByArtistId.getUserId());
                    optionalUser.ifPresent(us -> album.setArtistName(us.getLogin()));
                }
                user.get().setArtistId(artistId);
                UserDto userDto = new UserDto(user.get(), artistAlbums, likedAlbums);
                userDto.setFollowersUser(followerUsers);
                userDto.setFollowsUser(followUsers);
                Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserId(userId);
                subscription.ifPresent(subscription1 -> userDto.setSubscription(subscription.get()));
                return userDto;
            } else {
                throw new ServiceException(SERVICE_EXCEPTION.replace("0", ""));
            }
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public UserDto getUserLikedAlbums(Long userId) throws ServiceException {
        List<Album> likedAlbums = albumService.getLikedAlbums(userId);
        for (Album album : likedAlbums) {
            Artist artist = artistService.getUserByArtistId(album.getArtistId());
            album.setArtistName(getByIdUser(artist.getUserId()).getLogin());
        }
        return new UserDto(likedAlbums);
    }

    @Override
    public List<UserDto> getFollowerUsers(Long userId) throws ServiceException {
        try {
            List<User> followerUsers = userDao.getFollowerUsers(userId);
            List<UserDto> followerUserDto = new LinkedList<>();
            for (User u : followerUsers) {
                UserDto user = new UserDto(u, u.getId());
                Long artistId = artistService.getArtistIdByUserId(user.getId());
                user.setArtistId(artistId);
                followerUserDto.add(user);
            }
            return followerUserDto;
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<UserDto> getFollowUsers(Long userId) throws ServiceException {
        try {
            List<User> followUsers = userDao.getFollowUsers(userId);
            List<UserDto> followUserDto = new LinkedList<>();
            for (User u : followUsers) {
                UserDto user = new UserDto(u, u.getId());
                Long artistId = artistService.getArtistIdByUserId(user.getId());
                user.setArtistId(artistId);
                followUserDto.add(user);
            }
            return followUserDto;
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

    @Override
    public List<UserDto> getNotFollowUsers(Long userId) throws ServiceException {
        try {
            List<User> notFollowUsers = userDao.getNotFollowUsers(userId);

            List<UserDto> users = new LinkedList<>();
            for (User u : notFollowUsers) {
                Set<Role> userRoles = roleService.getUserRoles(u.getId());
                Long artistId = artistService.getArtistIdByUserId(u.getId());
                UserDto user = new UserDto(u, u.getId());
                user.setArtistId(artistId);
                user.setUserRoles(userRoles);
                if (!user.haveRole(RoleType.ADMIN.name())) {
                    users.add(user);
                }
            }
            return users;
        } catch (DaoException e) {
            LOGGER.error(SERVICE_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (Exception e) {
            LOGGER.error(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new ServiceException(SERVICE_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }

}
