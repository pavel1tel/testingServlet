package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class IndexCommand implements Command {

    UserService userService;

    public IndexCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean loggedIn = Boolean.parseBoolean(request.getSession().getAttribute("loggedIn").toString());
        if (loggedIn){
            long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            request.setAttribute("loggedIn", loggedIn);
            try {
                User user = userService.findById(userId);
                if (user.getRole().equals(Role.ROLE_USER)){
                    request.getSession().setAttribute("homeUrl", request.getContextPath() + "/app/userHome");
                } else if (user.getRole().equals(Role.ROLE_INSPECTOR)){
                    request.getSession().setAttribute("homeUrl", request.getContextPath() + "/app/inspHome");
                }
            } catch (UsernameNotFoundException ex) {
                ex.printStackTrace();
                return;
            }
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
