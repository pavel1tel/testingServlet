package com.kpi.testing.controller.command;

import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserHomeCommand implements Command {

    private final ReportService reportService;
    UserService userService;

    public UserHomeCommand(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (Boolean.parseBoolean(request.getSession().getAttribute("loggedIn").toString())) {
            Long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            try {
                User user = userService.findById(userId);
                List<ReportForUserReportTableDTO> reports = reportService.getAllByUserForUserTable(user);
                request.setAttribute("reports", reports);
                request.getRequestDispatcher("/WEB-INF/templates/home/userHome.jsp").forward(request, response);
            } catch (UsernameNotFoundException ex) {
                response.sendError(403);
            }
        } else {
            throw new RuntimeException();
        }
    }
}
