package com.kpi.testing.controller.filters;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Random;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("csrfToken", randomAlphaNumeric());
        se.getSession().setAttribute("loggedIn", false);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    public String randomAlphaNumeric() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
