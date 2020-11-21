package com.soundcloud.album;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.command.CommandUtil;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.subscription.SubscriptionStatus;
import com.soundcloud.track.Track;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "UPLOAD_ALBUM_COMMAND")
public class UploadAlbumCommand extends AbstractCommand {
    private final AlbumService albumService;
    private static final Logger LOGGER = LogManager.getLogger(UploadAlbumCommand.class);

    public UploadAlbumCommand(AlbumService albumService) {
        this.albumService = albumService;
    }
    /**
     * method upload user albums
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
                Long userId = currentUser.getId();
                String albumName = request.getParameter(REQUEST_PARAMETER_ALBUM_NAME);
                byte[] bytes = albumName.getBytes(StandardCharsets.ISO_8859_1);
                albumName = new String(bytes, StandardCharsets.UTF_8);

                AlbumState albumState = AlbumState.of(request.getParameter(REQUEST_PARAMETER_ALBUM_STATE));
                AlbumGenre albumGenre = AlbumGenre.of(request.getParameter(REQUEST_PARAMETER_ALBUM_GENRE));
                Album album = new Album(
                        albumName,
                        LocalDate.now(ZoneId.of("America/Montreal")),
                        0,
                        albumState,
                        albumGenre);

                List<Part> audio = request.getParts().stream().filter(p -> p.getName().equals("audio")).collect(Collectors.toList());
                List<Track> tracks = new LinkedList<>();
                List<String> errors = new LinkedList<>();
                if (audio.size() > MAX_TRACKS_AMOUNT) {
                    errors.add(COMMAND_MAX_TRACKS_AMOUNT_EXCEPTION);
                } else {
                    for (Part a : audio) {
                        if (a.getSize() <= TRACK_MAX_SIZE) {
                            File newFile = new File(COMMAND_UPLOAD_FILE_DIR, a.getSubmittedFileName());
                            newFile.createNewFile();
                            a.write(newFile.getAbsolutePath());
                            tracks.add(new Track(CommandUtil.getAudioName(a.getSubmittedFileName()), 0, COMMAND_TRACK_FILE_DIR + a.getSubmittedFileName()));
                        } else {
                            errors.add(COMMAND_TRACK_SIZE_EXCEPTION + a.getSubmittedFileName());
                            break;
                        }
                    }
                }
                Part image = request.getPart("image");
                if (image.getSize() >= IMAGE_MAX_SIZE) {
                    errors.add(COMMAND_IMAGE_SIZE_EXCEPTION);
                }
                if (!errors.isEmpty()) {
                    request.setAttribute(REQUEST_ERROR_ATTRIBUTE, errors);
                    forward(request, response, "?command=" + CommandType.GET_UPLOAD_PAGE_COMMAND);
                    return;
                }
                album.setAlbumIcon(image.getInputStream());
                albumService.uploadAlbum(album, tracks, userId);
                request.setAttribute("message", "Upload has been done successfully!");
                boolean subscribed = SubscriptionStatus.ACTIVE.equals(currentUser.getSubscription().getSubscriptionStatus());
                request.setAttribute(ApplicationConstants.REQUEST_PARAMETER_SUBSCRIBED, subscribed);
                request.setAttribute(REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                forward(request, response, "?command=" + CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND);
                return;
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
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
        forward(request, response, ApplicationPage.UPLOAD.getPagePath());

    }
}

