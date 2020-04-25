package com.kpi.testing.controller;

import com.kpi.testing.controller.command.*;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Servlet extends HttpServlet {
    private final Map<String, Command> getCommands = new HashMap<>();
    private final Map<String, Command> postCommands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        UserService userService = new UserService();
        getCommands.put("accounts/login", new LoginCommand());
        getCommands.put("index", new IndexCommand());
        getCommands.put("error", new ErrorCommand());
        getCommands.put("accounts/registration", new RegistrationCommand());

        postCommands.put("accounts/login", new PostLoginCommand(userService));

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
        Command command = commands.getOrDefault(path , new ErrorCommand());
        command.execute(request, response);
//        if(page.contains("redirect:")){
//            response.sendRedirect(request.getContextPath()+page.replace("redirect:", "/app"));
//        }else {
//            request.getRequestDispatcher(page).forward(request, response);
//        }
    }
}
