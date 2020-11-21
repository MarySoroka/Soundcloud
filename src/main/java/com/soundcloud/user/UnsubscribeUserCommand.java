package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.COMMAND_SUBSCRIBE_FATAL_EXCEPTION;

@Bean(beanName = "UNSUBSCRIBE_USER_COMMAND")
public class UnsubscribeUserCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(UnsubscribeUserCommand.class);

    public UnsubscribeUserCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method unsubscribe user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            Long userId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_USER_ID));
            UserDto user = userService.getByIdUser(userId);
            boolean subscribed = userService.unsubscribed(userId);
            if (subscribed){
                redirect(response, request.getContextPath()
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());

                return;
            }
            request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE,ApplicationConstants.COMMAND_SUBSCRIBE_EXCEPTION);
            forward(request,response,"?" + ApplicationConstants.COMMAND_NAME_PARAM +
                    "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());
        } catch (ServiceException e) {
            LOGGER.info(ApplicationConstants.COMMAND_SUBSCRIBE_EXCEPTION);
        } catch (Exception e) {
            LOGGER.error(ApplicationConstants.COMMAND_SUBSCRIBE_FATAL_EXCEPTION.replace("0", ""));
            throw new CommandExecuteException(COMMAND_SUBSCRIBE_FATAL_EXCEPTION.replace("0", ""), e);
        }
    }
}
