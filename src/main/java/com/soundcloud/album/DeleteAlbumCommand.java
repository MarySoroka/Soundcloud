package com.soundcloud.album;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.role.RoleType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.COMMAND_DELETE_EXCEPTION;


@Bean(beanName = "DELETE_ALBUM_COMMAND")
public class DeleteAlbumCommand extends AbstractCommand {
    private final AlbumService albumService;
    private static final Logger LOGGER = LogManager.getLogger(DeleteAlbumCommand.class);

    public DeleteAlbumCommand(AlbumService albumService) {
        this.albumService = albumService;
    }

    /**
     * method delete user album
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext instance = SecurityContext.getInstance();
            UserDto currentUser = instance.getCurrentUser();
            if (currentUser != null) {
                Long albumId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID));
                boolean deleted = albumService.deleteAlbum(albumId);
                if (deleted) {
                    if(currentUser.haveRole(RoleType.ADMIN.name())){
                        redirect(response,
                                request.getContextPath() + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ADMIN_USER_PAGE_COMMAND);
                        return;
                    }
                    redirect(response,
                            request.getContextPath() + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_USER_LIBRARY_COMMAND);
                }
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_DELETE_EXCEPTION.replace("0", "track"));
            throw new CommandExecuteException(COMMAND_DELETE_EXCEPTION.replace("0", "track"), e);
        }
    }
}
