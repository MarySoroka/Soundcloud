package com.soundcloud.filter;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.application.ApplicationPage;
import com.soundcloud.command.CommandType;
import com.soundcloud.security.SecurityContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "securityFilter", servletNames = {"index"})
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    /**
     * method set current user and check rights
     *
     * @param request  servlet request
     * @param response servlet response
     * @param chain    filter chain
     * @throws IOException      if we have troubles with filter
     * @throws ServletException if we have troubles with filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String id = ((HttpServletRequest) request).getSession().getId();
        SecurityContext securityContext = SecurityContext.getInstance();
        if (securityContext.isAuthorized(id)) {
            securityContext.setCurrentSessionId(id);
        }
        String command = request.getParameter(ApplicationConstants.COMMAND_NAME_PARAM);
        CommandType commandType = CommandType.of(command);
        if (securityContext.hasRights(commandType)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath()
                    + "?" + ApplicationConstants.COMMAND_NAME_PARAM +
                    "=" + CommandType.GET_PAGE_COMMAND + "&" + ApplicationConstants.REQUEST_PARAMETER_PAGE + "=" + ApplicationPage.LOGIN);
        }
    }

    @Override
    public void destroy() {

    }
}
