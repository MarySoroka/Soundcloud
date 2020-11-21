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
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "GET_ADMIN_USER_PAGE_COMMAND")
public class GetAdminUserPageCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(GetAdminUserPageCommand.class);
    private final UserService userService;

    public GetAdminUserPageCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method get admin page
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
            boolean authorized = SecurityContext.getInstance().isAuthorized(request.getSession().getId());
            request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, authorized);
            if (currentUser != null) {
                List<UserDto> users = userService.getAdminUsers();
                request.setAttribute(REQUEST_PARAMETER_USER, securityContext.getCurrentUser());
                request.setAttribute(REQUEST_PARAMETER_USERS,users);
                forward(request, response, ApplicationPage.ADMIN.getPagePath());
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}
