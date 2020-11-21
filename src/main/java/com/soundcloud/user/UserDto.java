package com.soundcloud.user;

import com.soundcloud.album.Album;
import com.soundcloud.builder.BuilderUtil;
import com.soundcloud.role.Role;
import com.soundcloud.subscription.Subscription;
import com.soundcloud.wallet.Wallet;

import java.io.IOException;
import java.util.*;

public class UserDto {
    private Long id;
    private String login;
    private String password;
    private String email;
    private Long walletId;

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    private Subscription subscription;

    public UserDto(Set<Role> roles) {
        this.userRoles = roles;
    }

    public List<UserDto> getFollowersUser() {
        return followersUser;
    }

    public void setFollowersUser(List<UserDto> followersUser) {
        this.followersUser = followersUser;
    }

    public List<UserDto> getFollowsUser() {
        return followsUser;
    }

    public void setFollowsUser(List<UserDto> followsUser) {
        this.followsUser = followsUser;
    }

    private List<UserDto> followersUser;
    private List<UserDto> followsUser;

    public UserDto(User user, List<Album> artistAlbums, List<Album> likedAlbums) throws IOException {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.userAlbums = artistAlbums;
        this.userFollows = user.getUserFollows();
        this.userFollowers = user.getUserFollowers();
        this.userIcon = BuilderUtil.getBase64(user.getUserIcon());
        this.artistId = user.getArtistId();
        this.walletId = user.getWalletId();
        this.userLikedAlbums = likedAlbums;

    }

    public UserDto(User user, Long id) throws IOException {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.userFollows = user.getUserFollows();
        this.userFollowers = user.getUserFollowers();
        this.userIcon = BuilderUtil.getBase64(user.getUserIcon());
        this.artistId = user.getArtistId();
        this.walletId = user.getWalletId();
    }

    public UserDto(List<Album> likedAlbums) {
        this.userLikedAlbums = likedAlbums;
    }


    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    private Set<Role> userRoles;
    private Integer userFollows;
    private Integer userFollowers;
    private String userIcon;
    private List<Album> userAlbums;
    private List<Album> userLikedAlbums;
    private Long artistId;

    public UserDto(Long id, String login, String password, String email, Set<Role> userRoles, Integer userFollows, Integer userFollowers, String userIcon, Wallet wallet, Set<UserDto> followUser, Set<UserDto> followersUser, List<Album> userAlbums, List<Album> userLikedAlbums) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.userRoles = userRoles;
        this.userFollows = userFollows;
        this.userFollowers = userFollowers;
        this.userIcon = userIcon;
        this.userAlbums = userAlbums;
        this.userLikedAlbums = userLikedAlbums;

    }


    public UserDto(UserDto user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.artistId = user.getArtistId();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.userRoles = user.getUserRoles();
        this.userFollows = user.getUserFollows();
        this.userFollowers = user.getUserFollowers();
        this.userIcon = user.getUserIcon();
        this.userAlbums = cloneUserAlbums(user.getUserAlbums());
        this.userLikedAlbums = cloneUserAlbums(user.getUserLikedAlbums());
        this.walletId = user.getWalletId();
        this.subscription = user.getSubscription();
    }


    public UserDto(User user, Set<Role> userRoles) throws IOException {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.userRoles = userRoles;
        this.userFollows = user.getUserFollows();
        this.userFollowers = user.getUserFollowers();
        this.userIcon = BuilderUtil.getBase64(user.getUserIcon());
        this.artistId = user.getArtistId();
        this.walletId = user.getWalletId();
    }

    public List<Album> getUserAlbums() {
        return userAlbums;
    }

    public void setUserAlbums(List<Album> userAlbums) {
        this.userAlbums = userAlbums;
    }


    private List<Album> cloneUserAlbums(List<Album> albumList) {
        List<Album> albums = new LinkedList<>();
        for (Album album : albumList) {
            albums.add(new Album(album));
        }
        return albums;
    }

    public List<Album> getUserLikedAlbums() {
        return userLikedAlbums;
    }

    public void setUserLikedAlbums(List<Album> userLikedAlbums) {
        this.userLikedAlbums = userLikedAlbums;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDTO = (UserDto) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(login, userDTO.login) &&
                Objects.equals(password, userDTO.password) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(userRoles, userDTO.userRoles) &&
                Objects.equals(userFollows, userDTO.userFollows) &&
                Objects.equals(userFollowers, userDTO.userFollowers) &&
                Objects.equals(userIcon, userDTO.userIcon) &&
                Objects.equals(userAlbums, userDTO.userAlbums) &&
                Objects.equals(userLikedAlbums, userDTO.userLikedAlbums);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, userRoles, userFollows, userFollowers, userIcon, userAlbums, userLikedAlbums);
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public boolean haveRole(String roleName) {
        for (Role role :
                this.userRoles) {
            if (role.getName().getType().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;

    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserFollows() {
        return userFollows;
    }

    public void setUserFollows(Integer userFollows) {
        this.userFollows = userFollows;
    }

    public Integer getUserFollowers() {
        return userFollowers;
    }

    public void setUserFollowers(Integer userFollowers) {
        this.userFollowers = userFollowers;
    }

}
