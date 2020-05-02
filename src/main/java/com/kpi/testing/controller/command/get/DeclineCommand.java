package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.UpdateReportDTO;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeclineCommand implements Command {
    private final ReportService reportService;
    private final UserService userService;

    public DeclineCommand(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
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
        Long reportId = Long.parseLong(path.replaceAll(".*/app/inspHome/decline/" , ""));
        if (user.getReportsInspected().stream().noneMatch(report -> report.getId().equals(reportId))){
            response.sendRedirect(request.getContextPath()+"/app" + "/error");
            return;
        }
        try {
            UpdateReportDTO updateReportDTO = reportService.getForUpdate(reportId);
            request.setAttribute("report_id", updateReportDTO.getId());
            request.getRequestDispatcher("/WEB-INF/templates/home/addReason.jsp").forward(request, response);
        } catch (UnknownReportError error) {
            throw new RuntimeException();
        }
    }
}
