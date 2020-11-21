package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.COMMAND_EXCEPTION;

@Bean(beanName = "GET_ALL_USERS_COMMAND")
public class GetAllUsersCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(GetAllUsersCommand.class);

    public GetAllUsersCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method get all users with their albums
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            List<User> allUsers = userService.getAllUsers();
            request.setAttribute("allUsers", allUsers);
            request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));

            forward(request,response,ApplicationPage.HOME.getPagePath());
        } catch (ServiceException e) {
            LOGGER.info(COMMAND_EXCEPTION);
            throw new CommandExecuteException(COMMAND_EXCEPTION, e);
        } catch (Exception e) {
            LOGGER.error(COMMAND_EXCEPTION);
            throw new CommandExecuteException(COMMAND_EXCEPTION, e);
        }
    }
}
