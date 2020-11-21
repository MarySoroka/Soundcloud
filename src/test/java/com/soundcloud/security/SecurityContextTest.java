package com.soundcloud.security;

import com.soundcloud.command.CommandType;
import com.soundcloud.role.Role;
import com.soundcloud.role.RoleType;
import com.soundcloud.user.User;
import com.soundcloud.user.UserDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

@RunWith(JUnit4.class)
public class SecurityContextTest {
    private final UserDto dto = new UserDto(new User(1L,
            "flash",
            "123",
            "m@gmail.com",
            this.getClass().getResourceAsStream("/avatar.png"),
            12,
            15, 1L), 1L);

    public SecurityContextTest() throws IOException {
    }

    private final SecurityContext securityContext = SecurityContext.getInstance();
    @Test
    public void getCurrentSessionId() {
        securityContext.setCurrentSessionId("");
        Assert.assertEquals("",securityContext.getCurrentSessionId());
    }
    @Test
    public void authorize() {
        securityContext.authorize(dto,"");
        Assert.assertTrue(securityContext.isAuthorized(""));
    }
    @Test
    public void updateCurrentUser(){
        securityContext.authorize(dto,"");
        dto.setPassword("111");
        securityContext.updateCurrentUser("",dto);
        Assert.assertEquals("111",securityContext.getCurrentUser().getPassword());

    }
    @Test
    public void haveRight() {
        SecurityContext.getInstance().initialize();
        dto.setUserRoles(new HashSet<>(Collections.singleton(new Role(RoleType.UNSIGNED_USER))));
        boolean haveRight = securityContext.hasRights(dto, CommandType.DELETE_ALBUM_COMMAND);
        Assert.assertFalse(haveRight);
        boolean haveRightTest = securityContext.hasRights(dto, CommandType.LOGOUT_USER_COMMAND);
        Assert.assertTrue(haveRightTest);
    }

}
