package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class DeclineCommand implements Command {
    private final ReportService reportService;
    private final UserService userService;
    private static final Logger logger = getLogger(DeclineCommand.class);

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
            logger.error("INVALID user recorded in session");
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        Long reportId = Long.parseLong(path.replaceAll(".*/app/inspHome/decline/" , ""));
        if (user.getReportsInspected().stream().noneMatch(report -> report.getId().equals(reportId))){
            logger.error("Inspector cant decline report with specified id");
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        try {
            Report updateReport = reportService.getById(reportId);
            if (!updateReport.getStatus().equals(ReportStatus.QUEUE)) {
                logger.error("Report is not in queue");
                response.sendRedirect(request.getContextPath() + "/app" + "/error");
                return;
            }
            request.setAttribute("report_id", updateReport.getId());
            request.getRequestDispatcher("/WEB-INF/templates/home/addReason.jsp").forward(request, response);
        } catch (UnknownReportError error) {
            logger.error("Report with specified id not found");
            throw new RuntimeException();
        }
    }
}
