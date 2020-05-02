package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.UpdateReportDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class PostChangeInspector implements Command {

    private final ReportService reportService;
    private final UserService userService;

    public PostChangeInspector(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getRequestURI();
        Long reportId = Long.parseLong(path.replaceAll(".*/app/userHome/change/" , ""));
        try {
            Report reportToChange = reportService.getById(reportId);
            reportService.changeInspector(reportToChange);
            response.sendRedirect(request.getContextPath()+"/app" + "/userHome");
        } catch (UnknownReportError | SQLException error) {
            throw new RuntimeException();
        }
    }
}
