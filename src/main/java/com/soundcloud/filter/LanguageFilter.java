package com.soundcloud.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@WebFilter(servletNames = {"index"}, filterName = "languageFilter")
public class LanguageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    /**
     * method change language
     *
     * @param request  servlet request
     * @param response servlet response
     * @param chain    filter chain
     * @throws IOException      if we have troubles with filter
     * @throws ServletException if we have troubles with filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            String lang = request.getParameter("lang");
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (("en".equalsIgnoreCase(lang) || "ru".equalsIgnoreCase(lang))) {
                Cookie langCookie = new Cookie("lang", lang);
                langCookie.setPath(httpRequest.getContextPath());
                httpRequest.setAttribute("lang", lang);
                ((HttpServletResponse) response).addCookie(langCookie);
            } else {
                Optional<Cookie[]> cookies = Optional.ofNullable(httpRequest.getCookies());
                Cookie langCookie = cookies.map(Stream::of).orElse(Stream.empty())
                        .filter(cookie -> cookie.getName().equalsIgnoreCase("lang")).findFirst()
                        .orElse(new Cookie("lang", "en"));
                langCookie.setPath(httpRequest.getContextPath());
                httpRequest.setAttribute("lang", langCookie.getValue());
                ((HttpServletResponse) response).addCookie(langCookie);
            }

        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
