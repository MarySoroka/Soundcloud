package com.soundcloud.track;

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

import static com.soundcloud.application.ApplicationConstants.COMMAND_DELETE_EXCEPTION;

@Bean(beanName = "DELETE_TRACK_COMMAND")
public class DeleteTrackCommand extends AbstractCommand {
    private final TrackService trackService;

    private static final Logger LOGGER = LogManager.getLogger(DeleteTrackCommand.class);

    public DeleteTrackCommand(TrackService trackService) {
        this.trackService = trackService;
    }
    /**
     * method delete track
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
                long albumId = Long.parseLong(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID));
                Long trackId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_TRACK_ID));
                boolean deleted = trackService.deleteTrack(trackId);
                if (deleted) {
                    redirect(response,
                            request.getContextPath() + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ALBUM_COMMAND + "&albumId=" + albumId);
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
