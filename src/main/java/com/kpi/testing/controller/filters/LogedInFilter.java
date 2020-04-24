package com.kpi.testing.controller.filters;

import com.kpi.testing.controller.security.PermissionResolver;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.LogRecord;

@Slf4j
public class LogedInFilter extends HttpFilter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        PermissionResolver pr = new PermissionResolver();
        session.setAttribute("username", pr.getUsername(session));
        super.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {

    }
}
