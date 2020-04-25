package com.kpi.testing.controller.security;

import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.PasswordMismatchException;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.UserService;
import com.kpi.testing.util.BCryptEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorizationInterceptor {
    UserService userService;
    BCryptEncoder encoder = new BCryptEncoder();

    public AuthorizationInterceptor(UserService userService) {
        this.userService = userService;
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        return userService.loadUserByEmail(email);
    }

    public User matchPasswords(User user, String password) throws PasswordMismatchException {
        if(encoder.matches(password, user.getPassword())){
            return user;
        } else {
            throw new PasswordMismatchException();
        }
    }

    public boolean isLoggedIn(HttpServletRequest request){
        return  (boolean) request.getSession().getAttribute("loggedIn");
    }

    public String createSession(HttpSession session, User user, String rememberMe) {
        session.setAttribute("user", user);
        if(rememberMe!=null && rememberMe.equals("on")){
            //todo dont work, create separate table with useer session and cookies for validation
            session.setMaxInactiveInterval(24*60*60);
        }
        return session.getId();
    }
}
