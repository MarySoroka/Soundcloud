package com.soundcloud.command;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.role.RoleType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.user.UserDto;
import com.soundcloud.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.COMMAND_SEARCH_EXCEPTION;

@Bean(beanName = "SEARCH_COMMAND")
public class SearchCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(SearchCommand.class);

    public SearchCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * method return search result by search data
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if Service exception has thrown
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            String searchData = request.getParameter(ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA);
            List<UserDto> users = userService.findUsersAlbumsByName(searchData);
            request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_USERS, users);
            SecurityContext securityContext = SecurityContext.getInstance();
            UserDto currentUser = securityContext.getCurrentUser();
            if (currentUser != null) {
                if (currentUser.haveRole(RoleType.ADMIN.name())) {
                    boolean isAdmin = currentUser.haveRole("admin");
                    request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_IS_ADMIN, isAdmin);
                }
            }
            forward(request, response, ApplicationPage.HOME.getPagePath());
        } catch (ServiceException e) {
            LOGGER.error(COMMAND_SEARCH_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_SEARCH_EXCEPTION.replace("0", e.toString()), e);

        }
    }
}