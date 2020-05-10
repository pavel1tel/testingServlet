package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.UpdateReportDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.InspectorService;
import com.kpi.testing.service.ReportOwnerService;
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
    private final ReportOwnerService reportOwnerService;

    public PostChangeInspector(ReportService reportService, UserService userService, ReportOwnerService reportOwnerService) {
        this.reportService = reportService;
        this.userService = userService;
        this.reportOwnerService = reportOwnerService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getRequestURI();
        User user;
        try {
            long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            user = userService.findById(userId);
        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath()+"/app" + "/error");
            return;
        }
        Long reportId = Long.parseLong(path.replaceAll(".*/app/userHome/change/" , ""));
        if (user.getReportsOwned().stream().noneMatch(report -> report.getId().equals(reportId))){
            response.sendRedirect(request.getContextPath()+"/app" + "/error");
            return;
        }
        try {
            Report reportToChange = reportService.getById(reportId);
            reportOwnerService.changeInspector(reportToChange);
            response.sendRedirect(request.getContextPath()+"/app" + "/userHome");
        } catch (UnknownReportError error) {
            throw new RuntimeException();
        }
    }
}