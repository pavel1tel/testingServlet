package com.kpi.testing.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class LogoutCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Arrays.stream(request.getCookies()).filter((c) -> c.getName().equals("ROLE_GUEST"))
                .forEach(cookie -> cookie.setValue("ROLE_GUEST"));
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/app" + "/accounts/login?logout=true");
    }
}
