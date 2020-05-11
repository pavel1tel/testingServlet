package com.kpi.testing.controller.filters;

import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class CSRFFilter implements Filter {

    private static final Logger logger = getLogger(CSRFFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getMethod().equals("POST")) {
            try {
                String csrfToken = Optional.ofNullable(request.getParameter("csrfToken")).orElseThrow(() -> new Exception("csrf mismatch"));
                if (!csrfToken.equals(request.getSession().getAttribute("csrfToken"))) {
                    logger.warn("csrf token is not equals to real one");
                    response.sendRedirect(request.getContextPath() + "/app" + "/error");
                    return;
                }
            } catch (Exception exception) {
                logger.warn("csrf token is not present in post request");
                response.sendRedirect(request.getContextPath() + "/app" + "/error");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
