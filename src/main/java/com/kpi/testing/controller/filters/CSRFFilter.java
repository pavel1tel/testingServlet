package com.kpi.testing.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CSRFFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getMethod().equals("POST")) {
            try {
                String csrfToken = Optional.ofNullable(request.getParameter("csrfToken")).orElseThrow(() -> new Exception("csrf mismatch"));
                if (!csrfToken.equals(request.getSession().getAttribute("csrfToken"))) {
                    response.sendRedirect(request.getContextPath() + "/app" + "/error");
                    return;
                }
            } catch (Exception exception) {
                response.sendRedirect(request.getContextPath() + "/app" + "/error");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
