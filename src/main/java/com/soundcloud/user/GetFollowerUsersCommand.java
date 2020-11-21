package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.COMMAND_LOGIN_EXCEPTION;

@Bean(beanName = "GET_FOLLOWER_USERS_COMMAND")
public class GetFollowerUsersCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(GetFollowerUsersCommand.class);

    public GetFollowerUsersCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method get follower users
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext securityContext = SecurityContext.getInstance();
            UserDto currentUser = securityContext.getCurrentUser();
            if (currentUser != null) {
                List<UserDto> followerUsers = userService.getFollowerUsers(currentUser.getId());
                request.setAttribute("followerUsers", followerUsers);
                redirect(response, request.getContextPath()
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND);
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_LOGIN_EXCEPTION.replace("0", e.getMessage()));
            throw new CommandExecuteException(COMMAND_LOGIN_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}
