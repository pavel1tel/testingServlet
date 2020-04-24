package com.kpi.testing.controller.command;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements Command{
    @Override
    public String execute(HttpServletRequest request) {
        return "/WEB-INF/templates/accounts/login.jsp";
    }
}
