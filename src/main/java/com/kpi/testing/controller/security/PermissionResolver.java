package com.kpi.testing.controller.security;

import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PermissionResolver {

     final Set<String> GUEST_PAGES = new HashSet<>(Arrays.asList("accounts/login", "index", "accounts/registration", "accounts/logout"));

    public boolean isInspector(User user){
        return user.getRole().equals(Role.ROLE_INSPECTOR);
    }

    public boolean isUser(User user){
        return user.getRole().equals(Role.ROLE_USER);
    }

    public boolean isAbleToAccess (HttpServletRequest request, String role) {

        System.out.println(role);
        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/" , "");

        if( path.contains("/tax/static/css/") || path.contains("/tax/static/js/") ) {
            return true;
        }

        if( role.equals("ROLE_GUEST") ){
            return GUEST_PAGES.contains(path);
        }
        return true;
    }
    
}
