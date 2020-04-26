package com.kpi.testing.controller.security;

import com.kpi.testing.exceptions.InvalidUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandler {

    public static void handle(Exception ex, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String clazz = ex.getClass().getName();
        switch (clazz){
            case("InvalidUserException"):
                response.sendRedirect(request.getContextPath()+"/app" + "/");
        }
    }
}
