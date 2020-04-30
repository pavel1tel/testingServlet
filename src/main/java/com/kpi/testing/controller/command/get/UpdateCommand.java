package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.AddReportDTO;
import com.kpi.testing.dto.UpdateReportDTO;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateCommand implements Command {

    private final ReportService reportService;

    public UpdateCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getRequestURI();
        Long reportId = Long.parseLong(path.replaceAll(".*/app/userHome/update/" , ""));
        try {
            UpdateReportDTO updateReportDTO = reportService.getForUpdate(reportId);
            request.setAttribute("report", updateReportDTO);
            request.getRequestDispatcher("/WEB-INF/templates/home/updateReport.jsp").forward(request, response);
        } catch (UnknownReportError error) {
            throw new RuntimeException();
        }
    }
}
