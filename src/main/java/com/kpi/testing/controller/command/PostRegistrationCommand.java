package com.kpi.testing.controller.command;

import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.InvalidUserException;
import com.kpi.testing.exceptions.UserExistsException;
import com.kpi.testing.service.UserService;
import com.kpi.testing.validators.NewUserValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostRegistrationCommand implements Command{
    UserService userService;

    public PostRegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = userService.extractUserFromRegistration(request);
        try {
            userService.registration(user);
        } catch (InvalidUserException | UserExistsException e) {
            response.sendRedirect(request.getContextPath() + "/app" + "/accounts/registration?error=true");
        }
        response.sendRedirect(request.getContextPath() + "/app" + "/accounts/login");
    }
}
