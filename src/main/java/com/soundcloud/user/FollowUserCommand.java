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

import static com.soundcloud.application.ApplicationConstants.COMMAND_FOLLOW_EXCEPTION;

@Bean(beanName = "FOLLOW_USER_COMMAND")
public class FollowUserCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(FollowUserCommand.class);

    public FollowUserCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method follow user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext securityContext = SecurityContext.getInstance();
            UserDto user = securityContext.getCurrentUser();
            if (user != null) {
                Long userId = user.getId();
                Long followsUserId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_FOLLOW_USER_ID));
                Long followersUserId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_FOLLOWER_USER_AMOUNT));
                Integer userFollows = user.getUserFollows();
                boolean follow = userService.followUser(userId, Long.valueOf(userFollows), followsUserId, followersUserId);
                if (follow) {
                    user.setUserFollows(++userFollows);
                    redirect(response, request.getContextPath()
                            + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                            "=" + CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND);
                }
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_FOLLOW_EXCEPTION.replace("0", e.getMessage()));
            throw new CommandExecuteException(COMMAND_FOLLOW_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}