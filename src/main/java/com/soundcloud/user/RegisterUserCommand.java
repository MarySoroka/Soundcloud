package com.soundcloud.user;


import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.PasswordEncodingException;
import com.soundcloud.security.SecurityUtil;
import com.soundcloud.service.ServiceException;
import com.soundcloud.validation.BeanValidator;
import com.soundcloud.validation.ValidationMessage;
import com.soundcloud.validation.ValidationResult;
import com.soundcloud.validation.ValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.COMMAND_UPLOAD_FATAL_EXCEPTION;

@Bean(beanName = "REGISTER_USER_COMMAND")
public class RegisterUserCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(RegisterUserCommand.class);
    private final BeanValidator beanValidator;
    private final UserService userService;

    public RegisterUserCommand(BeanValidator beanValidator, UserService userService) {
        this.beanValidator = beanValidator;
        this.userService = userService;
    }

    /**
     * method register user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            User user = new User(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_LOGIN),
                   request.getParameter(ApplicationConstants.REQUEST_PARAMETER_PASSWORD),
                    request.getParameter(ApplicationConstants.REQUEST_PARAMETER_EMAIL),0,0);
            Optional<ValidationResult> validationResult = beanValidator.validate(user);
            if (!validationResult.isPresent()) {
                user.setPassword( SecurityUtil.encode(user.getPassword()));
                if (userService.registerUser(user)){
                    request.setAttribute(ApplicationConstants.REQUEST_ARTIST_ID, user.getArtistId());
                    forward(request,response,"?" + ApplicationConstants.COMMAND_NAME_PARAM +
                            "=" + CommandType.GET_PAGE_COMMAND + "&" +
                            ApplicationConstants.REQUEST_PARAMETER_PAGE + "=" + ApplicationPage.LOGIN);
                }else{
                    throw new CommandExecuteException(ApplicationConstants.COMMAND_REGISTER_FATAL_EXCEPTION);
                }
            } else {
                request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE, validationResult.get().getMessageList());
                forward(request,response, ApplicationPage.REGISTRY.getPagePath());
            }
        } catch (ValidatorException e) {
            LOGGER.error(ApplicationConstants.COMMAND_REGISTER_EXCEPTION.replace("0",e.getMessage()));
            request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE,
                    Collections.singletonList(
                            new ValidationMessage(e.getMessage(),Collections.singletonList(e.getMessage()),"")));
            forward(request,response, ApplicationPage.REGISTRY.getPagePath());
        } catch (CommandExecuteException | PasswordEncodingException e) {
            LOGGER.error(ApplicationConstants.COMMAND_REGISTER_EXCEPTION.replace("0",e.getMessage()));
            request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE,
                    Collections.singletonList(
                            new ValidationMessage(e.getMessage(),Collections.singletonList(ApplicationConstants.COMMAND_REGISTER_EXCEPTION.replace("0",e.getMessage())),"")));
            forward(request,response, ApplicationPage.REGISTRY.getPagePath());
        } catch (ServiceException e) {
            LOGGER.error(ApplicationConstants.COMMAND_REGISTER_EXCEPTION.replace("0",e.getMessage()));
            request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE,
                    Collections.singletonList(
                            new ValidationMessage("",Collections.singletonList(e.getMessage()),"")));
            forward(request,response, ApplicationPage.REGISTRY.getPagePath());
        } catch (Exception e){
            LOGGER.error(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.getMessage()));
            throw new CommandExecuteException(COMMAND_UPLOAD_FATAL_EXCEPTION.replace("0", e.getMessage()), e);
        }

    }
}
