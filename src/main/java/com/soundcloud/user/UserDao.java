package com.soundcloud.user;

import com.soundcloud.dao.DaoException;
import com.soundcloud.dao.EntityDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
/**
 * User dao interface, that contains method for database interaction
 */
public interface UserDao extends EntityDao<User,Long> {
    /**
     * method find by login
     * @param login login
     * @return if user presents, then return optional of subscription, else optional.empty()
     * @throws DaoException if we get exception in sql request
     * @throws SQLException if we get exception in sql request
     */
    Optional<User> findByLogin(String login) throws  DaoException, SQLException;
    /**
     * method find by login
     * @param login login
     * @return if user presents, then return optional of subscription, else optional.empty()
     * @throws DaoException if we get exception in sql request
     * @throws SQLException if we get exception in sql request
     */
    Optional<User> findByName(String login) throws  DaoException, SQLException;

    /**
     * method return follow users
     * @param userId user id
     * @return list of users
     * @throws DaoException if we get exception in sql request
     */
    List<User> getFollowerUsers(Long userId) throws DaoException;
    /**
     * method return follower users
     * @param userId user id
     * @return list of users
     * @throws DaoException if we get exception in sql request
     */
    List<User> getFollowUsers(Long userId) throws DaoException;

    /**
     * method unfollow user
     * @param userId user id
     * @param followsAmount follow amount
     * @param followUserId follow user id
     * @param followersAmount followers amount
     * @return true if success, else false
     * @throws DaoException if we get exception in sql request
     */
    boolean unfollowUser(Long userId, Integer followsAmount, Long followUserId, Integer followersAmount) throws DaoException;
    /**
     * method follow user
     * @param userId user id
     * @param followsAmount follow amount
     * @param followUserId follow user id
     * @param followersAmount followers amount
     * @return true if success, else false
     * @throws DaoException if we get exception in sql request
     */
    boolean followUser(Long userId,Long followsAmount, Long followUserId, Long followersAmount) throws DaoException;
    /**
     * method return not follow users
     * @param userId user id
     * @return list of users
     * @throws DaoException if we get exception in sql request
     */
    List<User> getNotFollowUsers(Long userId) throws DaoException;


}
