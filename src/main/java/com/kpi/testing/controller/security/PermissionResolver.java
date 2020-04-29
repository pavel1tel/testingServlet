package com.kpi.testing.controller.security;

import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Supplier;

public class PermissionResolver {
    private final Map<Role, Set<String>> permissions = new HashMap<>();
    final Set<String> COMMON_PAGES = new HashSet<>(Arrays.asList("index", "accounts/logout", ".*/tax/static/css/.*", ".*/tax/static/js/.*"));
    final Set<String> GUEST_PAGES = new HashSet<>(Arrays.asList("accounts/login", "accounts/registration"));
    final Set<String> USER_PAGES = new HashSet<>(Arrays.asList("userHome/.*", "userHome"));
    final Set<String> INSP_PAGES = new HashSet<>(Arrays.asList("inspHome/.*", "inspHome"));

    public PermissionResolver() {
        permissions.put(Role.ROLE_GUEST, GUEST_PAGES);
        permissions.put(Role.ROLE_USER, USER_PAGES);
        permissions.put(Role.ROLE_INSPECTOR, INSP_PAGES);
    }

    public boolean isAbleToAccess (HttpServletRequest request, Role role) {

        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/" , "");

        if( COMMON_PAGES.stream().anyMatch(path::matches)){
            return true;
        }

        return permissions.get(role).stream().anyMatch(path::matches);
    }
    
}
