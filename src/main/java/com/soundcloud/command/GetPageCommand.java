package com.soundcloud.command;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.bean.Bean;
import com.soundcloud.security.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Bean(beanName = "GET_PAGE_COMMAND")
public class GetPageCommand extends AbstractCommand {
    /**
     * method return page by it url
     *
     * @param request  http request http request
     * @param response http response http response
     */
    @Override
    public void executeCommand(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(ApplicationConstants.REQUEST_AUTHORIZED_ATTRIBUTE, SecurityContext.getInstance().isAuthorized(request.getSession().getId()));
        forward(request, response, ApplicationPage.of(request.getParameter(ApplicationConstants.REQUEST_PARAMETER_PAGE)).getPagePath());
    }
}