package com.soundcloud.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface release command execute
 */

public interface Command {
    /**
     * method execute command
     *
     * @param request  http request
     * @param response http response
     */
    void execute(HttpServletRequest request, HttpServletResponse response);

}
