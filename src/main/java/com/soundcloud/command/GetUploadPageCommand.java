package com.soundcloud.command;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.subscription.Subscription;
import com.soundcloud.subscription.SubscriptionStatus;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.soundcloud.application.ApplicationConstants.*;

@Bean(beanName = "GET_UPLOAD_PAGE_COMMAND")
public class GetUploadPageCommand extends AbstractCommand {

    private static final Logger LOGGER = LogManager.getLogger(GetUploadPageCommand.class);

    /***
     * method return upload page
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if Service exception has thrown
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext securityContext = SecurityContext.getInstance();
            UserDto currentUser = securityContext.getCurrentUser();
            request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
            if (currentUser != null) {
                Subscription subscription = currentUser.getSubscription();
                boolean status = subscription != null && SubscriptionStatus.ACTIVE.equals(subscription.getSubscriptionStatus());
                request.setAttribute(REQUEST_PARAMETER_SUBSCRIBED, status);
                request.setAttribute(REQUEST_PARAMETER_USER, securityContext.getCurrentUser());
                forward(request, response, ApplicationPage.UPLOAD.getPagePath());
            } else {
                forward(request, response, ApplicationPage.LOGIN.getPagePath());
            }
        } catch (Exception e) {
            LOGGER.error(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }
    }
}