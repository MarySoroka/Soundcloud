package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.command.Command;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.mock;
@RunWith(JUnit4.class)
public class LogoutUserCommandTest {
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private final SecurityContext securityContext = mock(SecurityContext.class);
    @Mock
    private final HttpSession session = mock(HttpSession.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    @Test
    public void logoutCommandExecuteIsRight() throws IOException {
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();
        Mockito.doNothing().when(request).setAttribute(ApplicationConstants.REQUEST_ARTIST_ID, 1L);
        Mockito.doReturn("").when(request).getContextPath();
        Mockito.doNothing().when(response).sendRedirect(
                "" + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND +
                        "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + 1L);
        Command command = new LogoutUserCommand();
        command.execute(request, response);
        Assert.assertNull(SecurityContext.getInstance().getCurrentUser());
    }

}