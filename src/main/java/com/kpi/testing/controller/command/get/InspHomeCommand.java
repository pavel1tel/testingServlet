package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.ReportForInspectorReportTableDTO;
import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class InspHomeCommand implements Command {
    private final ReportService reportService;
    private final UserService userService;

    public InspHomeCommand(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (Boolean.parseBoolean(request.getSession().getAttribute("loggedIn").toString())) {
            Long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            try {
                User user = userService.findById(userId);
                List<ReportForInspectorReportTableDTO> reports = reportService.getAllByInspectorAndStatusForTable(user, ReportStatus.QUEUE);
                request.setAttribute("reports", reports);
                request.getRequestDispatcher("/WEB-INF/templates/home/inspHome.jsp").forward(request, response);
            } catch (UsernameNotFoundException ex) {
                response.sendError(403);
            }
        } else {
            throw new RuntimeException();
        }
    }
}
