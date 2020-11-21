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

@Bean(beanName = "GET_ALL_USER_ALBUMS_COMMAND")
public class GetAllUserAlbumsCommand extends AbstractCommand {

    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(GetAllUserAlbumsCommand.class);

    public GetAllUserAlbumsCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method get all user  albums
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
            UserDto user;
            if (currentUser != null) {
                boolean isCurrentUser = false;
                long artistId = Long.parseLong(request.getParameter(ApplicationConstants.REQUEST_ARTIST_ID));
                Long userId = userService.getUserByArtistId(artistId);

                boolean isAdmin = currentUser.haveRole("admin");
                if (userId.equals(currentUser.getId())) {
                    isCurrentUser = true;
                }
                user = userService.getUserProfile(userId, artistId);
                boolean subscribed = userService.subscribed(currentUser);
                if (subscribed && userId.equals(currentUser.getId())){
                    currentUser.setUserRoles(user.getUserRoles());
                }
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_SUBSCRIPTION, currentUser.getSubscription());
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_SUBSCRIBED, subscribed);
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_USER, user);
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_IS_ADMIN, isAdmin);
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_IS_CURRENT_USER, isCurrentUser);
                request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                forward(request, response, ApplicationPage.PROFILE.getPagePath());
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_GET_ALL_ALBUMS_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}
