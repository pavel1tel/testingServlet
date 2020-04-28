package com.kpi.testing.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        if (session.getAttribute("loggedIn") != null) {
            boolean loggedIn = Boolean.parseBoolean(session.getAttribute("loggedIn").toString());
            if (!loggedIn) {
                request.getRequestDispatcher("/WEB-INF/templates/denied.jsp").forward(request, servletResponse);
                return;
            } из нет
        } else {
            request.getRequestDispatcher("/WEB-INF/templates/denied.jsp").forward(request, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
