package com.kpi.testing.controller.filters;

import com.kpi.testing.controller.security.PermissionResolver;
import com.kpi.testing.entity.User;
import jdk.nashorn.internal.runtime.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.LogRecord;

public class LoggedInFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        PermissionResolver pr = new PermissionResolver();
        Cookie roleCookie;
        if(session.getAttribute("user") != null){
            User user = (User) session.getAttribute("user");
            session.setAttribute("username", user.getUsername());
            session.setAttribute("loggedIn", true);
            roleCookie = new Cookie("role", user.getRole().name());
            roleCookie.setDomain(request.getServerName());
            roleCookie.setPath(request.getContextPath());
            response.addCookie(roleCookie);
        } else {
            session.setAttribute("loggedIn", false);
            roleCookie = new Cookie("role", "ROLE_GUEST");
            roleCookie.setDomain(request.getServerName());
            roleCookie.setPath(request.getContextPath());
            response.addCookie(roleCookie);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }



    @Override
    public void destroy() {

    }
}
