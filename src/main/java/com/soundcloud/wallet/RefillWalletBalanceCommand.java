package com.soundcloud.wallet;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;
import com.soundcloud.service.ServiceException;
import com.soundcloud.user.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

import static com.soundcloud.application.ApplicationConstants.COMMAND_EXCEPTION;

@Bean(beanName = "REFILL_WALLET_BALANCE_COMMAND")
public class RefillWalletBalanceCommand extends AbstractCommand {
    private final WalletService walletService;
    private static final Logger LOGGER = LogManager.getLogger(RefillWalletBalanceCommand.class);

    public RefillWalletBalanceCommand(WalletService walletService) {
        this.walletService = walletService;
    }
    /**
     * method refill wallet balance
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            SecurityContext securityContext = SecurityContext.getInstance();
            UserDto currentUser = securityContext.getCurrentUser();
            if (currentUser != null) {
                BigDecimal walletAmount = new BigDecimal(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_WALLET_AMOUNT));
                Long walletId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_WALLET_ID));
                walletService.setWalletAmount(walletId, new Wallet(walletId, walletAmount));
                request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
                request.setAttribute(ApplicationConstants.REQUEST_ARTIST_ID, currentUser.getArtistId());
                redirect(response,
                        request.getContextPath()
                                + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                                "=" + CommandType.GET_ALL_USER_ALBUMS_COMMAND + "&" + ApplicationConstants.REQUEST_ARTIST_ID + "=" + currentUser.getArtistId());
            }
        } catch (ServiceException e) {
            LOGGER.info(COMMAND_EXCEPTION);
            throw new CommandExecuteException(COMMAND_EXCEPTION, e);
        } catch (Exception e) {
            LOGGER.error(COMMAND_EXCEPTION);
            throw new CommandExecuteException(COMMAND_EXCEPTION, e);
        }
    }
}
