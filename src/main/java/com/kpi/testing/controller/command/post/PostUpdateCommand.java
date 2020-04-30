package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.UpdateReportDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class PostUpdateCommand implements Command {

    private final ReportService reportService;

    public PostUpdateCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getRequestURI();
        Long reportId = Long.parseLong(path.replaceAll(".*/app/userHome/update/" , ""));
        try {
            Report reportToUpdate = reportService.getById(reportId);
            UpdateReportDTO updateReportDTO =new UpdateReportDTO();
            String NAME_PARAM = "name";
            String name = request.getParameter(NAME_PARAM);
            String DESC_PARAM = "description";
            String description = request.getParameter(DESC_PARAM);
            updateReportDTO.setId(reportId);
            updateReportDTO.setDeclineReason(reportToUpdate.getDeclineReason());
            updateReportDTO.setName(name);
            updateReportDTO.setDescription(description);
            reportService.update(reportToUpdate, updateReportDTO);
            response.sendRedirect(request.getContextPath()+"/app" + "/userHome");
        } catch (UnknownReportError | SQLException error) {
            throw new RuntimeException();
        }
    }
}
