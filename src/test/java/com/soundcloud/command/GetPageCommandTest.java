package com.soundcloud.command;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.user.User;
import com.soundcloud.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.soundcloud.application.ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE;
import static com.soundcloud.application.ApplicationConstants.REQUEST_PARAMETER_USER;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class GetPageCommandTest {
    @Mock
    private final HttpSession session = mock(HttpSession.class);
    @Mock
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    @Mock
    @Spy
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    @Spy
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final User user = new User(
            "flash",
            "123",
            "m@gmail.com",
            0,
            0);

    @Test
    public void executeCommand() throws CommandExecuteException, ServletException, IOException {
        user.setArtistId(1L);
        user.setId(1L);
        UserDto userDto = new UserDto(user, 1L);
        SecurityContext.getInstance().authorize(userDto, "");
        Mockito.doReturn(ApplicationPage.UPLOAD.name()).when(request).getParameter(ApplicationConstants.REQUEST_PARAMETER_PAGE);

        Mockito.doNothing().when(request).setAttribute(REQUEST_AUTHORIZED_ATTRIBUTE, false);
        Mockito.doReturn(session).when(request).getSession();
        Mockito.doReturn("").when(session).getId();

        Mockito.doNothing().when(request).setAttribute(REQUEST_PARAMETER_USER, user);

        Mockito.doReturn(requestDispatcher).when(request).getRequestDispatcher(ApplicationPage.UPLOAD.getPagePath());
        Mockito.doNothing().when(requestDispatcher).forward(request, response);
        Command command = new GetPageCommand();
        command.execute(request,response);
        Mockito.verify(request).setAttribute(REQUEST_AUTHORIZED_ATTRIBUTE, true);

    }
}
