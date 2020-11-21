package com.soundcloud.album;

import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "EDIT_ALBUM_COMMAND")
public class EditAlbumCommand extends AbstractCommand {
    private final AlbumService albumService;
    private static final Logger LOGGER = LogManager.getLogger(EditAlbumCommand.class);

    public EditAlbumCommand(AlbumService albumService) {
        this.albumService = albumService;
    }

    /**
     * method save edit album information
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            Long albumId = Long.valueOf(request.getParameter(REQUEST_PARAMETER_ALBUM_ID));
            Album albumById = albumService.getAlbumById(albumId);
            String albumName = request.getParameter(REQUEST_PARAMETER_ALBUM_NAME);
            if (albumName != null) {
                byte[] bytes = albumName.getBytes(StandardCharsets.ISO_8859_1);
                albumName = new String(bytes, StandardCharsets.UTF_8);
                albumById.setName(albumName);
            }
            String requestAlbumState = request.getParameter(REQUEST_PARAMETER_ALBUM_STATE);
            String requestAlbumGenre = request.getParameter(REQUEST_PARAMETER_ALBUM_GENRE);
            if (requestAlbumState != null) {
                albumById.setAlbumState(AlbumState.of(requestAlbumState));
            }
            if (requestAlbumGenre != null) {
                albumById.setAlbumGenre(AlbumGenre.of(requestAlbumGenre));
            }
            Part image = request.getPart(REQUEST_PARAMETER_IMAGE);
            if (image.getInputStream().available() == 0) {
                albumService.updateAlbum(albumById.getId(), albumById);
            } else {
                albumById.setAlbumIcon(image.getInputStream());
                albumService.updateAlbumWithIcon(albumById.getId(), albumById);
            }
            request.setAttribute(REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
            forward(request, response, "?command=" + CommandType.GET_USER_LIBRARY_COMMAND);
            return;
        } catch (IOException e) {
            LOGGER.error(COMMAND_UPLOAD_SAVE_FILE_EXCEPTION.replace("0", e.getMessage()));
        } catch (ServletException e) {
            LOGGER.error(COMMAND_UPLOAD_EXCEPTION.replace("0", e.getMessage()));
            throw new CommandExecuteException(COMMAND_UPLOAD_EXCEPTION.replace("0", e.getMessage()), e);
        } catch (ServiceException e) {
            LOGGER.error(COMMAND_UPLOAD_EXCEPTION.replace("0", e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", ""));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", ""), e);
        }
        request.setAttribute(REQUEST_ERROR_ATTRIBUTE,
                Collections.singletonList(COMMAND_UPLOAD_CLIENT_EXCEPTION));
        forward(request, response, ApplicationPage.ALBUM.getPagePath());
    }


}
