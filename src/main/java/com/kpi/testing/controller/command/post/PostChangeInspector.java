package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.controller.command.get.UpdateCommand;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.ReportOwnerService;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class PostChangeInspector implements Command {

    private final ReportService reportService;
    private final UserService userService;
    private final ReportOwnerService reportOwnerService;
    private static final Logger logger = getLogger(UpdateCommand.class);

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
            logger.error("INVALID user recorded in session");
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        Long reportId = Long.parseLong(path.replaceAll(".*/app/userHome/change/", ""));
        if (user.getReportsOwned().stream().noneMatch(report -> report.getId().equals(reportId))) {
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        try {
            Report reportToChange = reportService.getById(reportId);
            if (!reportToChange.getStatus().equals(ReportStatus.NOT_ACCEPTED)) {
                logger.error("Report is not not_accepted");
                response.sendRedirect(request.getContextPath() + "/app" + "/error");
                return;
            }
            reportOwnerService.changeInspector(reportToChange);
            response.sendRedirect(request.getContextPath() + "/app" + "/userHome");
        } catch (UnknownReportError error) {
            logger.error("Report with specified id not found");
            throw new RuntimeException();
        }
    }
}
