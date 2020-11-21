package com.soundcloud.album;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.COMMAND_LIKE_EXCEPTION;

@Bean(beanName = "SAVE_LIKE_ALBUM_COMMAND")
public class SaveLikeAlbumCommand extends AbstractCommand {
    private final AlbumService albumService;
    private static final Logger LOGGER = LogManager.getLogger(SaveLikeAlbumCommand.class);

    public SaveLikeAlbumCommand(AlbumService albumService) {
        this.albumService = albumService;
    }
    /**
     * method save user liked album
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            Long albumId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID));
            Integer likesAmount = Integer.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_LIKES_AMOUNT));
            SecurityContext instance = SecurityContext.getInstance();
            UserDto currentUser = instance.getCurrentUser();
            if (currentUser != null) {
                boolean isSaved = albumService.saveLikedAlbum(albumId, currentUser.getId(),likesAmount);
                if (isSaved) {
                    redirect(response,
                            request.getContextPath()+"?"+ApplicationConstants.COMMAND_NAME_PARAM+"="+CommandType.GET_ALBUM_COMMAND + "&albumId=" + albumId);
                }
            }else{
                request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_LIKE_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_LIKE_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}
