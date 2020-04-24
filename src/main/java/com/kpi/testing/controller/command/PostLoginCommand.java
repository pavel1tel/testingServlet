package com.kpi.testing.controller.command;

import javax.servlet.http.HttpServletRequest;

public class PostLoginCommand implements Command{
    @Override
    public String execute(HttpServletRequest request) {
        return "redirect:/index";
    }
}
