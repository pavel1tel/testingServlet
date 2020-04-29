package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.ReportDTO;
import com.kpi.testing.dto.ReportForInspectorReportTableDTO;
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

public class PostAddCommand implements Command {

    private final ReportService reportService;
    private final UserService userService;

    public PostAddCommand(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String NAME_PARAM = "name";
        String name = request.getParameter(NAME_PARAM);
        String DESC_PARAM = "description";
        String description = request.getParameter(DESC_PARAM);
        ReportDTO report = new ReportDTO();
        report.setName(name);
        report.setDescription(description);
        Long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
        try {
            User user = userService.findById(userId);
            reportService.save(report, user);
            response.sendRedirect(request.getContextPath()+"/app" + "/userHome");
        } catch (UsernameNotFoundException ex) {
            response.sendError(403);
        }
    }
}
