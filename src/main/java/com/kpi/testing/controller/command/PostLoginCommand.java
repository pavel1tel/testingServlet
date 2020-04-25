package com.kpi.testing.controller.command;

import com.kpi.testing.controller.security.AuthorizationInterceptor;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Status;
import com.kpi.testing.exceptions.PasswordMismatchException;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class PostLoginCommand implements Command{
    private final AuthorizationInterceptor authorizationInterceptor;
    UserService userService;

    public PostLoginCommand(UserService userService){
        this.userService = userService;
        authorizationInterceptor = new AuthorizationInterceptor(userService);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!authorizationInterceptor.isLoggedIn(request)) {
            HttpSession session = request.getSession();
            User user;
            try {
                String EMAIL_PARAM = "email";
                String email = request.getParameter(EMAIL_PARAM);
                String PASSWORD_PARAM = "password";
                String password = request.getParameter(PASSWORD_PARAM);
                String rememberMe = request.getParameter("remember-me");
                user = authorizationInterceptor.loadUserByEmail(email);
                user = authorizationInterceptor.matchPasswords(user, password);
                if(user.getStatus().equals(Status.Deleted)){
                    response.sendRedirect(request.getContextPath() + "/app" + "/accounts/login?error");
                }
                String sessionID = authorizationInterceptor.createSession(session, user, rememberMe);
            } catch (UsernameNotFoundException | PasswordMismatchException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect(request.getContextPath() + "/app" + "/accounts/login?error");
                return;
            }
        }
        response.sendRedirect(request.getContextPath()+"/app" + "/index");
    }
}
