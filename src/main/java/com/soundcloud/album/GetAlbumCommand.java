package com.soundcloud.album;


import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "GET_ALBUM_COMMAND")
public class GetAlbumCommand extends AbstractCommand {

    private final AlbumService albumService;
    private static final Logger LOGGER = LogManager.getLogger(GetAlbumCommand.class);

    public GetAlbumCommand(AlbumService albumService) {
        this.albumService = albumService;
    }
    /**
     * method get album by album id
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext securityContext = SecurityContext.getInstance();
            Long albumId = Long.valueOf(request.getParameter(REQUEST_PARAMETER_ALBUM_ID));
            String artistAlbumName = albumService.getArtistAlbumName(albumId);
            Album album = albumService.getAlbumById(albumId);
            boolean isOwn = false;
            Long artistId = album.getArtistId();
            UserDto currentUser = securityContext.getCurrentUser();
            if (currentUser != null) {
                boolean liked = albumService.isLiked(securityContext.getCurrentUser().getId(), albumId);
                request.setAttribute(REQUEST_PARAMETER_IS_LIKED, liked);
                album.setArtistName(artistAlbumName);
                boolean isAdmin = currentUser.haveRole("admin");
                if (currentUser.getArtistId().equals(artistId)||isAdmin) {
                    isOwn = true;
                }
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_IS_ADMIN, isAdmin);
            }
            request.setAttribute(REQUEST_PARAMETER_ALBUM, album);
            request.setAttribute(ApplicationConstants.REQUEST_ARTIST_ID, artistId);
            request.setAttribute(REQUEST_PARAMETER_ALBUM_IS_OWN, isOwn);
            request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
            forward(request, response, ApplicationPage.ALBUM.getPagePath());
        } catch (Exception e) {
            LOGGER.error(ApplicationConstants.COMMAND_GET_ALBUM_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(ApplicationConstants.COMMAND_GET_ALBUM_EXCEPTION.replace("0", e.toString()));
        }
    }
}