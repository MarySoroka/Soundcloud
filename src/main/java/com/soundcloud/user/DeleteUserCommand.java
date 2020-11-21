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

import static com.soundcloud.application.ApplicationConstants.COMMAND_DELETE_EXCEPTION;

@Bean(beanName = "DELETE_USER_COMMAND")
public class DeleteUserCommand extends AbstractCommand {
    private final UserService userService;

    private static final Logger LOGGER = LogManager.getLogger(DeleteUserCommand.class);

    public DeleteUserCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * method delete user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext instance = SecurityContext.getInstance();
            UserDto currentUser = instance.getCurrentUser();
            if (currentUser != null) {
                long userId = Long.parseLong(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_USER_ID));
                boolean deleted = userService.deleteUser(userId);
                if (deleted) {
                    if (!currentUser.getId().equals(userId)) {
                        instance.deleteUserById(userId);
                        redirect(response,
                                request.getContextPath() + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ADMIN_USER_PAGE_COMMAND);
                    }else{
                        instance.deleteUser(request.getSession().getId());
                        redirect(response,
                                request.getContextPath() + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND);

                    }
                }
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_DELETE_EXCEPTION.replace("0", "track"));
            throw new CommandExecuteException(COMMAND_DELETE_EXCEPTION.replace("0", "user"), e);
        }
    }
}
