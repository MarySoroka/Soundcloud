package com.soundcloud.user;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.validation.ValidationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import static com.soundcloud.application.ApplicationConstants.COMMAND_SUBSCRIBE_FATAL_EXCEPTION;

@Bean(beanName = "SUBSCRIBE_USER_COMMAND")
public class SubscribeUserCommand extends AbstractCommand {
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(SubscribeUserCommand.class);

    public SubscribeUserCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * method subscribe user
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            Long userId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_USER_ID));
            BigDecimal walletAmount = new BigDecimal(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_WALLET_AMOUNT));
            UserDto user = userService.getByIdUser(userId);
            BigDecimal previousWalletAmount = userService.getUserWalletAmount(userId);
            BigDecimal currentWalletAmount = walletAmount.add(previousWalletAmount);
            if (currentWalletAmount.compareTo(new BigDecimal(8)) < 0) {
                request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE, Collections.singleton(new ValidationMessage(ApplicationConstants.REQUEST_PARAMETER_WALLET_AMOUNT, new LinkedList<>(Collections.singleton("Wallet amount should be equals or more then 8$")), currentWalletAmount.toString())));
                forward(request, response, "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());

            }
            userService.addUserWallet(userId, currentWalletAmount);
            boolean subscribed = userService.subscribeUser(userId, ApplicationConstants.SUBSCRIBE_AMOUNT);
            if (subscribed) {
                SecurityContext securityContext = SecurityContext.getInstance();
                UserDto currentUser = securityContext.getCurrentUser();
                if (currentUser != null) {
                    userService.subscribed(currentUser);
                }
                redirect(response, request.getContextPath()
                        + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                        "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());

                return;
            }
            request.setAttribute(ApplicationConstants.REQUEST_ERROR_ATTRIBUTE, ApplicationConstants.COMMAND_SUBSCRIBE_EXCEPTION);
            forward(request, response, "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                    "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + user.getArtistId());
        } catch (ServiceException e) {
            LOGGER.info(ApplicationConstants.COMMAND_SUBSCRIBE_EXCEPTION);
        } catch (Exception e) {
            LOGGER.error(ApplicationConstants.COMMAND_SUBSCRIBE_FATAL_EXCEPTION.replace("0", ""));
            throw new CommandExecuteException(COMMAND_SUBSCRIBE_FATAL_EXCEPTION.replace("0", ""), e);
        }
    }
}
