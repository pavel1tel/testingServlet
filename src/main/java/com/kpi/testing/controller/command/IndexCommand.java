package com.kpi.testing.controller.command;

import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class IndexCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean loggedIn = Boolean.parseBoolean(session.getAttribute("loggedIn").toString());
        if (loggedIn) {
            User user = (User) session.getAttribute("user");
            if (user.getRole().equals(Role.ROLE_USER)){
                request.getSession().setAttribute("homeUrl", request.getContextPath() + "/app/userHome");
            } else if (user.getRole().equals(Role.ROLE_INSPECTOR)){
                request.getSession().setAttribute("homeUrl", request.getContextPath() + "/app/inspHome");
            }
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
