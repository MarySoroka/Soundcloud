package com.soundcloud.track;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.command.CommandUtil;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "UPLOAD_TRACK_COMMAND")
public class UploadTrackCommand extends AbstractCommand {
    private final TrackService trackService;

    private static final Logger LOGGER = LogManager.getLogger(UploadTrackCommand.class);

    public UploadTrackCommand(TrackService trackService) {
        this.trackService = trackService;
    }
    /**
     * method upload track
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
                List<Part> audio = request.getParts().stream().filter(p -> p.getName().equals(REQUEST_PARAMETER_AUDIO)).collect(Collectors.toList());
                for (Part a : audio) {
                    File newFile = new File(COMMAND_UPLOAD_FILE_DIR, a.getSubmittedFileName());
                    newFile.createNewFile();
                    a.write(newFile.getAbsolutePath());
                    Track track = new Track(CommandUtil.getAudioName(a.getSubmittedFileName()), 0, COMMAND_TRACK_FILE_DIR + a.getSubmittedFileName());
                    track.setAlbumId(albumId);
                    trackService.saveTrack(track);
                }
                redirect(response,
                        request.getContextPath() + "?"
                                + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ALBUM_COMMAND + "&albumId=" + albumId);

            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(ApplicationConstants.COMMAND_DELETE_EXCEPTION.replace("0", "track"));
            throw new CommandExecuteException(COMMAND_LIKE_EXCEPTION.replace("0", "track"), e);
        }
    }
}
