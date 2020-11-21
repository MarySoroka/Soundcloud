package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.role.RoleType;
import com.soundcloud.security.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.COMMAND_GET_ALL_ALBUMS_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.COMMAND_UPLOAD_FATAL_EXCEPTION;

@Bean(beanName = "GET_ALL_USER_ARTIST_ALBUMS_COMMAND")
public class GetAllUserArtistAlbumsCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(GetAllUserArtistAlbumsCommand.class);

    public GetAllUserArtistAlbumsCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * method get list of all user with their albums
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
            List<UserDto> users;
            if (currentUser != null) {
                if (!currentUser.haveRole(RoleType.ADMIN.name())) {
                    Long userId = currentUser.getId();
                    users = userService.getAllArtistAlbums(userId);
                    List<UserDto> notFollowUsers = userService.getNotFollowUsers(userId);
                    UserDto user = userService.getUserLikedAlbums(userId);
                    currentUser.setUserLikedAlbums(user.getUserLikedAlbums());
                    request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_USER, currentUser);
                    request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_NOT_FOLLOW_USERS, notFollowUsers);
                } else {
                    boolean isAdmin = currentUser.haveRole("admin");
                    request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_IS_ADMIN, isAdmin);
                    users = userService.getAllArtistAlbums();
                }
            } else {
                users = userService.getAllArtistAlbums();
            }
            request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_USERS, users);
            request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
            forward(request, response, ApplicationPage.HOME.getPagePath());
        } catch (Exception e) {
            LOGGER.error(COMMAND_GET_ALL_ALBUMS_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}
