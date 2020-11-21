package com.soundcloud.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(filterName = "encodingFilter", servletNames = {"index"})
public class CharacterEncodingFilter implements Filter {

    public static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * method change character encoding
     *
     * @param request  servlet request
     * @param response servlet response
     * @param chain    filter chain
     * @throws IOException      if we have troubles with filter
     * @throws ServletException if we have troubles with filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String characterEncoding = request.getCharacterEncoding();
        if (!UTF_8.equalsIgnoreCase(characterEncoding)) {
            request.setCharacterEncoding(UTF_8);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
