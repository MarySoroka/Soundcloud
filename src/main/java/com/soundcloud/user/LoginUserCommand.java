package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.role.Role;
import com.soundcloud.role.RoleType;
import com.soundcloud.security.PasswordEncodingException;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.security.SecurityUtil;
import com.soundcloud.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.soundcloud.application.ApplicationConstants.COMMAND_LOGIN_EXCEPTION;

@Bean(beanName = "LOGIN_USER_COMMAND")
public class LoginUserCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(LoginUserCommand.class);

    public LoginUserCommand(UserService userService) {
        this.userService = userService;
    }
    /**
     * method login user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            String login = request.getParameter(ApplicationConstants.REQUEST_PARAMETER_LOGIN);
            byte[] bytes = login.getBytes(StandardCharsets.ISO_8859_1);
            login = new String(bytes, StandardCharsets.UTF_8);

            String password = SecurityUtil.encode(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_PASSWORD));
            Optional<UserDto> loginUser = userService.loginUser(login, password);

            if (loginUser.isPresent()) {
                SecurityContext.getInstance().authorize(loginUser.get(), request.getSession().getId());
                Set<Role> userRoles = loginUser.get().getUserRoles();
                Optional<Role> adminRole = userRoles.stream().filter(u -> RoleType.ADMIN.equals(u.getName())).findFirst();
                if (adminRole.isPresent()) {
                    request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                    redirect(response,
                            request.getContextPath()
                                    + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                                    "=" + CommandType.GET_ADMIN_USER_PAGE_COMMAND);
                    return;
                }
                SecurityContext.getInstance().authorize(loginUser.get(), request.getSession().getId());
                request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                request.setAttribute(ApplicationConstants.REQUEST_ARTIST_ID, loginUser.get().getArtistId());
                redirect(response,
                        request.getContextPath()
                                + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                                "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + loginUser.get().getArtistId());
                return;
            }
        } catch (ServiceException | PasswordEncodingException e) {
            LOGGER.info(COMMAND_LOGIN_EXCEPTION);
        } catch (Exception e) {
            LOGGER.error(COMMAND_LOGIN_EXCEPTION.replace("0", ""));
            throw new CommandExecuteException(COMMAND_LOGIN_EXCEPTION.replace("0", ""), e);
        }
        request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE, Collections.singleton(COMMAND_LOGIN_EXCEPTION));
        forward(request, response, ApplicationPage.LOGIN.getPagePath());
    }
}
