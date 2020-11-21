package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.COMMAND_LOGIN_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.COMMAND_LOGOUT_EXCEPTION;

@Bean(beanName = "LOGOUT_USER_COMMAND")
public class LogoutUserCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(LogoutUserCommand.class);
    /**
     * method logout user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {

            SecurityContext.getInstance().unauthorized(request.getSession().getId());
            redirect(response, request.getContextPath()
                    + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                    "=" + CommandType.GET_PAGE_COMMAND + "&" +
                    ApplicationConstants.REQUEST_PARAMETER_PAGE + "=" + ApplicationPage.LOGIN);
        } catch (Exception e) {
            LOGGER.error(COMMAND_LOGOUT_EXCEPTION.replace("0", e.getMessage()));
            throw new CommandExecuteException(COMMAND_LOGIN_EXCEPTION.replace("0", e.getMessage()), e);

        }
    }
}
