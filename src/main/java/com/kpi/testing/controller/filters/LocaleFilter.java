package com.kpi.testing.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class LocaleFilter implements Filter{
    private static final String CURRENT_LANGUAGE = "locale";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        String lang = request.getParameter("lang");
        if (lang == null){
            setDefaultsIfMissing(session);
        } else {
            setLocale(session, lang);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void setDefaultsIfMissing(HttpSession session) {
        String langCode = (String) session.getAttribute(CURRENT_LANGUAGE);
        if (langCode == null) {
            setLocale(session, "en");
        }
    }

    public void setLocale(HttpSession session, String langCode) {
        Locale locale = new Locale(langCode);
        session.setAttribute(CURRENT_LANGUAGE, locale.getLanguage());
    }

    @Override
    public void destroy() {

    }
}
