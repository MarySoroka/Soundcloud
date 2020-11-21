package com.soundcloud.role;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RoleTypeTest {
    @Test
    public void getByNameRoleTypeDefaultIsEmpty() {
        String type = "DEFAULT";
        RoleType roleType = RoleType.of(type);
        assertNotNull(roleType);
    }

    @Test
    public void getByNameRoleTypeSignedUserIsRight() {
        String type = "signed_User";
        RoleType roleType = RoleType.of(type);
        assertNotNull(roleType);
    }

    @Test
    public void getByNameRoleTypeNotSignedUserIsRight() {
        String type = "unsigned_User";
        RoleType roleType = RoleType.of(type);
        assertNotNull(roleType);
    }

    @Test
    public void getByNameRoleTypeAdminIsRight() {
        String type = "admin";
        RoleType roleType = RoleType.of(type);
        assertNotNull(roleType);
    }
}
