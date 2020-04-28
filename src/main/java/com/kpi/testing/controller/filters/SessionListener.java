package com.kpi.testing.controller.filters;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("loggedIn", false);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
