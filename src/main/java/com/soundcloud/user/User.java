package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.validation.*;

import java.io.InputStream;

@Validate(value = "user")
public class User {
    private Long id;

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    private Long artistId;

    public User(Long id, String login, String password, String email, InputStream userIcon, Integer userFollows, Integer userFollowers, Long walletId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.userIcon = userIcon;
        this.userFollows = userFollows;
        this.userFollowers = userFollowers;
        this.walletId = walletId;
    }

    @NotEmpty
    @MinLength(3)
    @MaxLength(30)
    private String login;
    @NotEmpty
    @MinLength(3)
    @MaxLength(30)
    private String password;
    @NotEmpty
    @Regex(regex = ApplicationConstants.VALIDATION_EMAIL_REGEX)
    private String email;

    private InputStream userIcon = this.getClass().getResourceAsStream("/avatar.png");

    private Integer userFollows;
    private Integer userFollowers;

    private Long walletId;

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public User(String login, String password, String email, Integer userFollows, Integer userFollowers) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.userFollows = userFollows;
        this.userFollowers = userFollowers;
    }


    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.userIcon = user.getUserIcon();
        this.userFollows = user.getUserFollows();
        this.userFollowers = user.getUserFollowers();
        this.walletId = user.getWalletId();
        this.artistId = user.getArtistId();
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

    public InputStream getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(InputStream userIcon) {
        this.userIcon = userIcon;
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
