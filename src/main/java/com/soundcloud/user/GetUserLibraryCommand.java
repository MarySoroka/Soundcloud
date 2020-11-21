package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.security.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.COMMAND_GET_ALL_ALBUMS_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.COMMAND_UPLOAD_FATAL_EXCEPTION;

@Bean(beanName = "GET_USER_LIBRARY_COMMAND")
public class GetUserLibraryCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(GetUserLibraryCommand.class);

    public GetUserLibraryCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * method get user library
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
                UserDto userDto = userService.getUserLibrary(currentUser.getId());
                request.setAttribute("user", userDto);
                boolean subscribed = userService.subscribed(currentUser);
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_SUBSCRIBED, subscribed);
                request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                forward(request, response, ApplicationPage.LIBRARY.getPagePath());
            } else {
                request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_GET_ALL_ALBUMS_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}
