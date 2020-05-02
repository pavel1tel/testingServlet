package com.kpi.testing.controller;

import com.kpi.testing.controller.command.*;
import com.kpi.testing.controller.command.get.*;
import com.kpi.testing.controller.command.get.UserHomeCommand;
import com.kpi.testing.controller.command.post.*;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Servlet extends HttpServlet {
    private final Map<String, Command> getCommands = new HashMap<>();
    private final Map<String, Command> postCommands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        UserService userService = new UserService();
        ReportService reportService = new ReportService();
        getCommands.put("accounts/login", new LoginCommand());
        getCommands.put("index", new IndexCommand(userService));
        getCommands.put("error", new ErrorCommand());
        getCommands.put("accounts/registration", new RegistrationCommand());
        getCommands.put("accounts/logout", new LogoutCommand());
        getCommands.put("userHome", new UserHomeCommand(reportService, userService));
        getCommands.put("inspHome", new InspHomeCommand(reportService, userService));
        getCommands.put("userHome/add", new AddCommand());
        getCommands.put("userHome/update/[1-9]*", new UpdateCommand(reportService));

        postCommands.put("accounts/login", new PostLoginCommand(userService));
        postCommands.put("accounts/registration", new PostRegistrationCommand(userService));
        postCommands.put("userHome/add", new PostAddCommand(reportService, userService));
        postCommands.put("userHome/update/[1-9]*", new PostUpdateCommand(reportService));
        postCommands.put("userHome/change/[1-9]*", new PostChangeInspector(reportService,userService));

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, postCommands);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, getCommands);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Command> commands) throws IOException,
            ServletException {
        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/" , "");
        String match = commands.keySet().stream().filter(path::matches).findFirst().orElse("error");
        Command command = commands.getOrDefault(match , new ErrorCommand());
        command.execute(request, response);
    }
}
