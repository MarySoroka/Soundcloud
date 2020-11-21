package com.soundcloud.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class UserFieldTest {
    @Test
    public void getByNameUserFieldDefaultIsEmpty() {
        String type = "DEFAULT";
        Optional<UserField> roleType = UserField.of(type);
        assertFalse(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldIdIsEmpty() {
        String type = "user_id";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldPasswordIsEmpty() {
        String type = "user_password";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldEmailIsEmpty() {
        String type = "user_email";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldLoginIsEmpty() {
        String type = "user_login";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldRolesIsEmpty() {
        String type = "user_roles";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldFollowsIsEmpty() {
        String type = "user_follows";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldFollowersIsEmpty() {
        String type = "user_followers";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldWalletIdIsEmpty() {
        String type = "wallet_id";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldIconIsEmpty() {
        String type = "user_icon";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldFollowsUserIsEmpty() {
        String type = "follows_user";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldFollowersUserIsEmpty() {
        String type = "followers_user";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldUserAlbumsIsEmpty() {
        String type = "user_albums";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldUserLikedAlbumsIsEmpty() {
        String type = "user_liked_albums";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldUserPlaylistsIsEmpty() {
        String type = "user_playlists";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameUserFieldUserLikedPlaylistsIsEmpty() {
        String type = "user_liked_playlists";
        Optional<UserField> roleType = UserField.of(type);
        assertTrue(roleType.isPresent());
    }

}
