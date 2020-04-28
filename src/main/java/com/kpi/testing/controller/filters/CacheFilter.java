package com.kpi.testing.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CacheFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
            filterChain.doFilter(servletRequest, servletResponse);
        }

        @Override
        public void destroy() {

        }

}
