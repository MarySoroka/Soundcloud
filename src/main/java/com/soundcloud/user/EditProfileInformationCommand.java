package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

import static com.soundcloud.application.ApplicationConstants.COMMAND_UPLOAD_FATAL_EXCEPTION;

@Bean(beanName = "EDIT_PROFILE_INFORMATION_COMMAND")
public class EditProfileInformationCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(EditProfileInformationCommand.class);

    public EditProfileInformationCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method save changes of profile information
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        Long artistId = null;
        try {
            SecurityContext securityContext = SecurityContext.getInstance();
            UserDto currentUser = securityContext.getCurrentUser();
            if (currentUser != null) {
                Long userId = Long.parseLong(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_USER_ID));
                User user = userService.getUser(userId);
                artistId = currentUser.getArtistId();
                String userLogin = request.getParameter(ApplicationConstants.REQUEST_PARAMETER_LOGIN);
                String userEmail = request.getParameter(ApplicationConstants.REQUEST_PARAMETER_EMAIL);
                InputStream userIcon = request.getPart(ApplicationConstants.REQUEST_PARAMETER_IMAGE).getInputStream();
                if (userIcon.available() == 0) {
                    userIcon = user.getUserIcon();
                }
                User updateUser = new User(userId, userLogin, user.getPassword(), userEmail, userIcon, user.getUserFollows(), user.getUserFollowers(), user.getWalletId());
                updateUser.setArtistId(currentUser.getArtistId());
                boolean isUpdate = userService.updateUserInformation(updateUser);
                if (isUpdate) {
                    UserDto byIdUser = userService.getByIdUser(currentUser.getId());
                    currentUser.setUserIcon(byIdUser.getUserIcon());
                    securityContext.updateCurrentUser(request.getSession().getId(), currentUser);
                    redirect(response, request.getContextPath()
                            + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                            "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());
                }
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (ServiceException e) {

            redirect(response,request.getContextPath()
                    + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                    "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + artistId);

        } catch (Exception e) {
            LOGGER.error(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }
    }
}