package com.kpi.testing.controller.security;

import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;

import javax.servlet.http.HttpSession;

public class PermissionResolver {

    public boolean isInspector(User user){
        return user.getRole().equals(Role.ROLE_INSPECTOR);
    }

    public boolean isUser(User user){
        return user.getRole().equals(Role.ROLE_USER);
    }

    public boolean isLoggedIn(HttpSession session){
        return session.getAttribute("user") != null;
    }

    public User getUser(HttpSession session){
        return (User) session.getAttribute("user");
    }
}
