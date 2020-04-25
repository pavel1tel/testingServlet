package com.kpi.testing.controller.filters;

import com.kpi.testing.controller.security.PermissionResolver;
import com.kpi.testing.entity.User;
import jdk.nashorn.internal.runtime.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.LogRecord;

public class LoggedInFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        PermissionResolver pr = new PermissionResolver();
        if(session.getAttribute("user") != null){
            User user = (User) session.getAttribute("user");
            session.setAttribute("username", user.getUsername());
            session.setAttribute("loggedIn", true);
        } else {
            session.setAttribute("loggedIn", false);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }



    @Override
    public void destroy() {

    }
}
