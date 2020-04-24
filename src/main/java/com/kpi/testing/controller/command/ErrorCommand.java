package com.kpi.testing.controller.command;

import javax.servlet.http.HttpServletRequest;

public class ErrorCommand implements Command{
    @Override
    public String execute(HttpServletRequest request) {
        return "/WEB-INF/templates/error.jsp";
    }
}
