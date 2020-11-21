package com.soundcloud.role;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;
import com.soundcloud.command.AbstractCommand;
import com.soundcloud.command.CommandExecuteException;
import com.soundcloud.command.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.soundcloud.application.ApplicationConstants.COMMAND_UPDATE_ROLES_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.COMMAND_UPDATE_ROLES_FATAL_EXCEPTION;

@Bean(beanName = "UPDATE_ROLES_COMMAND")
public class UpdateRolesCommand extends AbstractCommand {
    private final RoleService roleService;
    private static final Logger LOGGER = LogManager.getLogger(UpdateRolesCommand.class);

    public UpdateRolesCommand(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * method update user roles
     *
     * @param request  http request
     * @param response http response
     * @throws CommandExecuteException if we get Exception or ServiceException
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException {
        try {
            String[] updateRolesId = request.getParameterValues(ApplicationConstants.REQUEST_PARAMETER_USER_ROLE);
            Long userId = Long.valueOf(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_USER_ID));
            List<Role> updateRoles = new LinkedList<>();
            Arrays.stream(updateRolesId).forEach(roleId->updateRoles.add(new Role(userId, Long.valueOf(roleId))));
            roleService.updateUserRoles(userId,updateRoles);
            redirect(response,
                    request.getContextPath() + "?" + ApplicationConstants.COMMAND_NAME_PARAM + "=" + CommandType.GET_ADMIN_USER_PAGE_COMMAND);

        } catch (Exception e) {
            LOGGER.error(COMMAND_UPDATE_ROLES_EXCEPTION.replace("0", e.toString()));
            throw new CommandExecuteException(COMMAND_UPDATE_ROLES_FATAL_EXCEPTION.replace("0", e.toString()), e);
        }

    }
}
