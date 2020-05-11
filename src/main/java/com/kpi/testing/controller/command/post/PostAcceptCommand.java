package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.InspectorService;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class PostAcceptCommand implements Command {
    private final ReportService reportService;
    private static final Logger logger = getLogger(PostAcceptCommand.class);
    private final InspectorService inspectorService;
    private final UserService userService;

    public PostAcceptCommand(ReportService reportService, UserService userService, InspectorService inspectorService) {
        this.reportService = reportService;
        this.userService = userService;
        this.inspectorService = inspectorService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getRequestURI();
        User user;
        try {
            long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            user = userService.findById(userId);
        } catch (Exception ex) {
            logger.error("INVALID user recorded in session");
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        Long reportId = Long.parseLong(path.replaceAll(".*/app/inspHome/accept/", ""));
        if (user.getReportsInspected().stream().noneMatch(report -> report.getId().equals(reportId))) {
            logger.error("Inspector cant accept report with specified id");
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        try {
            Report report = reportService.getById(reportId);
            if (!report.getStatus().equals(ReportStatus.QUEUE)) {
                logger.error("Report is not in queue");
                response.sendRedirect(request.getContextPath() + "/app" + "/error");
                return;
            }
            inspectorService.acceptReport(report, user);
            response.sendRedirect(request.getContextPath() + "/app" + "/inspHome");
        } catch (UnknownReportError error) {
            logger.error("Report with specified id not found");
            throw new RuntimeException();
        }
    }
}
