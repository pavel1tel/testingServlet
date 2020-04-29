package com.kpi.testing.controller.filters;

import com.kpi.testing.controller.security.PermissionResolver;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        PermissionResolver pr = new PermissionResolver();
        UserService userService = new UserService();
        Role role = Role.ROLE_GUEST;
        if (Boolean.parseBoolean(request.getSession().getAttribute("loggedIn").toString())) {
            Long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            try {
                User user = userService.findById(userId);
                role = user.getRole();
            } catch (UsernameNotFoundException e) {
                response.sendError(403);
            }
        }
        if(!pr.isAbleToAccess(request, role)) {
            request.getRequestDispatcher("/WEB-INF/templates/denied.jsp").forward(request, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
