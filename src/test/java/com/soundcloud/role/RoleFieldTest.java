package com.soundcloud.role;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RoleFieldTest {
    @Test
    public void getByNameRoleTypeDefaultIsEmpty() {
        String type = "DEFAULT";
        Optional<RoleField> roleType = RoleField.of(type);
        assertFalse(roleType.isPresent());
    }

    @Test
    public void getByNameRoleFieldRoleNameIsRight() {
        String type = "role_name";
        Optional<RoleField> roleType = RoleField.of(type);
        assertTrue(roleType.isPresent());
    }

    @Test
    public void getByNameRoleFieldRoleIdIsRight() {
        String type = "role_id";
        Optional<RoleField> roleType = RoleField.of(type);
        assertTrue(roleType.isPresent());
    }
    @Test
    public void getByNameRoleFieldUserIdIsRight() {
        String type = "user_id";
        Optional<RoleField> roleType = RoleField.of(type);
        assertTrue(roleType.isPresent());
    }
}
