package com.kpi.testing.controller.command;

import com.kpi.testing.exceptions.UsernameNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {
    void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
