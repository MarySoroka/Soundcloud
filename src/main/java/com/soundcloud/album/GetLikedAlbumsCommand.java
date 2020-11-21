package com.soundcloud.album;

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
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "GET_LIKED_ALBUMS_COMMAND")
public class GetLikedAlbumsCommand extends AbstractCommand {
    private final AlbumService albumService;
    private static final Logger LOGGER = LogManager.getLogger(GetLikedAlbumsCommand.class);

    public GetLikedAlbumsCommand(AlbumService albumService) {
        this.albumService = albumService;
    }
    /**
     * method get user liked albums album
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            UserDto currentUser = SecurityContext.getInstance().getCurrentUser();
            if (currentUser != null) {
                List<Album> userAlbums = albumService.getLikedAlbums(currentUser.getId());
                request.setAttribute(REQUEST_PARAMETER_ALBUM_LIKED, userAlbums);
                redirect(response,
                        request.getContextPath()
                                + "?" + COMMAND_NAME_PARAM +
                                "=" + CommandType.GET_PAGE_COMMAND + "&" +
                                REQUEST_PARAMETER_PAGE + "=" + request.getParameter(REQUEST_PARAMETER_PAGE));
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_GET_ALL_ALBUMS_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}
