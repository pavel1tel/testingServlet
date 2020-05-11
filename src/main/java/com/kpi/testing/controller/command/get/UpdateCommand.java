package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.UpdateReportDTO;
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

public class UpdateCommand implements Command {

    private final ReportService reportService;
    private final UserService userService;
    private static final Logger logger = getLogger(UpdateCommand.class);


    public UpdateCommand(ReportService reportService, UserService userService) {
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
        Long reportId = Long.parseLong(path.replaceAll(".*/app/userHome/update/" , ""));
        if (user.getReportsOwned().stream().noneMatch(report -> report.getId().equals(reportId))){
            logger.error("User cant update report with specified id");
            response.sendRedirect(request.getContextPath() + "/app" + "/error");
            return;
        }
        try {
            Report updateReport = reportService.getById(reportId);
            if (!updateReport.getStatus().equals(ReportStatus.NOT_ACCEPTED)) {
                logger.error("Report is not not_accepted");
                response.sendRedirect(request.getContextPath() + "/app" + "/error");
                return;
            }
            UpdateReportDTO updateReportDTO = new UpdateReportDTO();
            updateReportDTO.setName(updateReport.getName());
            updateReportDTO.setDescription(updateReport.getDescription());
            updateReportDTO.setDeclineReason(updateReport.getDeclineReason());
            updateReportDTO.setId(updateReport.getId());
            request.setAttribute("report", updateReportDTO);
            request.getRequestDispatcher("/WEB-INF/templates/home/updateReport.jsp").forward(request, response);
        } catch (UnknownReportError error) {
            logger.error("Report with specified id not found");
            throw new RuntimeException();
        }
    }
}
