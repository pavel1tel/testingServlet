package com.kpi.testing.controller.command;

import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserHomeCommand implements Command {

    private final ReportService reportService;

    public UserHomeCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        boolean loggedIn = Boolean.parseBoolean(session.getAttribute("loggedIn").toString());
        if (loggedIn) {
            User user = (User) session.getAttribute("user");
            List<ReportForUserReportTableDTO> reports = reportService.getAllByUserForUserTable(user);
            request.setAttribute("reports", reports);
            request.getRequestDispatcher("/WEB-INF/templates/home/userHome.jsp").forward(request, response);
        } else {
            throw new RuntimeException();
        }
    }
}
