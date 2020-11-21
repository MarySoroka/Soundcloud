package com.soundcloud.user;

import com.soundcloud.role.Role;
import com.soundcloud.service.ServiceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * method find by login
     *
     * @param login    login
     * @param password password
     * @return if user presents, then return optional of subscription, else optional.empty()
     * @throws ServiceException if we get exception from dao
     */
    Optional<UserDto> loginUser(String login, String password) throws ServiceException;

    /**
     * method register user
     *
     * @param user user
     * @return true, if register, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean registerUser(User user) throws ServiceException;

    /**
     * method follow user
     *
     * @param userId          user id
     * @param followsAmount   follow amount
     * @param followUserId    follow user id
     * @param followersAmount followers amount
     * @return true if success, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean followUser(Long userId, Long followsAmount, Long followUserId, Long followersAmount) throws ServiceException;

    /**
     * method unfollow user
     *
     * @param userId          user id
     * @param followsAmount   follow amount
     * @param followUserId    follow user id
     * @param followersAmount followers amount
     * @return true if success, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean unfollowUser(Long userId, Integer followsAmount, Long followUserId, Integer followersAmount) throws ServiceException;

    /**
     * method return all admins users
     *
     * @return list of users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> getAdminUsers() throws ServiceException;

    /**
     * method delete subscription
     *
     * @param userId user id
     * @return if user has been deleted, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean deleteUser(Long userId) throws ServiceException;

    /**
     * method return user
     *
     * @param artistId artist id
     * @return user id
     * @throws ServiceException if we get exception from dao
     */
    Long getUserByArtistId(Long artistId) throws ServiceException;

    /**
     * method return user dto
     *
     * @param userId   user id
     * @param artistId artist id
     * @return user dto
     * @throws ServiceException if we get exception from dao
     */
    UserDto getUserProfile(Long userId, Long artistId) throws ServiceException;

    /**
     * method return all users
     *
     * @return all users
     * @throws ServiceException if we get exception from dao
     */
    List<User> getAllUsers() throws ServiceException;

    /**
     * method return user by id
     *
     * @param userId user id
     * @return user dto
     * @throws ServiceException if we get exception from dao
     */
    UserDto getByIdUser(Long userId) throws ServiceException;

    /**
     * method return user by id
     *
     * @param userId user id
     * @return user
     * @throws ServiceException if we get exception from dao
     */
    User getUser(Long userId) throws ServiceException;

    /**
     * method return user library
     *
     * @param userId user id
     * @return user dto
     * @throws ServiceException if we get exception from dao
     */
    UserDto getUserLibrary(Long userId) throws ServiceException;

    /**
     * method get all user liked albums
     *
     * @param userId user id
     * @return user
     * @throws ServiceException if we get exception from dao
     */
    UserDto getUserLikedAlbums(Long userId) throws ServiceException;

    /**
     * method return follower users
     *
     * @param userId user id
     * @return list of users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> getFollowerUsers(Long userId) throws ServiceException;

    /**
     * method return follower users
     *
     * @param userId user id
     * @return list of users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> getFollowUsers(Long userId) throws ServiceException;

    /**
     * method return follower users
     *
     * @param userId user id
     * @return list of users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> getNotFollowUsers(Long userId) throws ServiceException;

    /**
     * method return all users
     *
     * @param userId user id
     * @return list of users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> getAllArtistAlbums(Long userId) throws ServiceException;

    /**
     * method return all artist
     *
     * @return list of users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> getAllArtistAlbums() throws ServiceException;

    /**
     * method update subscription
     *
     * @param user user
     * @return if user has been update, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean updateUserInformation(User user) throws ServiceException;

    /**
     * method subscription
     *
     * @param userId user id
     * @return if user has been subscribe, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean subscribeUser(Long userId, BigDecimal subscribeAmount) throws ServiceException;

    /**
     * method update user roles
     *
     * @param userId user id
     * @throws ServiceException if we get exception from dao
     */
    void updateUserRoles(Long userId, List<Role> userRoles) throws ServiceException;

    /**
     * method return user wallet amount
     *
     * @param userId user id
     * @return wallet amount
     * @throws ServiceException if we get exception from dao
     */
    BigDecimal getUserWalletAmount(Long userId) throws ServiceException;

    /**
     * method add user wallet
     *
     * @param userId       user id
     * @param walletAmount wallet amount
     * @throws ServiceException if we get exception from dao
     */
    void addUserWallet(Long userId, BigDecimal walletAmount) throws ServiceException;

    /**
     * method check subscription
     *
     * @param userDto user
     * @return if user has been subscribe, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean subscribed(UserDto userDto) throws ServiceException;

    /**
     * method unsubscribe
     *
     * @param userId user id
     * @return if user has been unsubscribe, then return true, else false
     * @throws ServiceException if we get exception from dao
     */
    boolean unsubscribed(Long userId) throws ServiceException;

    /**
     * method return all user albums find by name
     *
     * @param userLogin user login
     * @return all find users
     * @throws ServiceException if we get exception from dao
     */
    List<UserDto> findUsersAlbumsByName(String userLogin) throws ServiceException;

}
