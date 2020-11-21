package com.soundcloud.command;

import com.soundcloud.application.ApplicationPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AbstractCommand.class);

    /**
     * method do forward
     *
     * @param request  http request
     * @param response http response
     * @param url      forward address
     */
    public void forward(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to forward view", e);
        }

    }

    /**
     * method do redirect
     *
     * @param response http response
     * @param url      response address
     */
    protected void redirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to forward view", e);
        }

    }

    /**
     * method execute command
     *
     * @param request  http request
     * @param response http response
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            executeCommand(request, response);
        } catch (CommandExecuteException e) {
            LOGGER.error("Failed to execute command ", e);
            forward(request, response, ApplicationPage.ERROR.getPagePath());
        }
    }

    /**
     * method execute command
     *
     * @param request  http request
     * @param response http response
     */
    public abstract void executeCommand(HttpServletRequest request, HttpServletResponse response) throws CommandExecuteException;
}