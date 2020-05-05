package com.kpi.testing.controller.command.get;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import com.kpi.testing.util.Pagination;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class UserHomeCommand implements Command {

    private final ReportService reportService;
    private final UserService userService;

    public UserHomeCommand(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (Boolean.parseBoolean(request.getSession().getAttribute("loggedIn").toString())) {
            Long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
            try {
                int page;
                try {
                    page = Integer.parseInt(Optional.ofNullable(request.getParameter("page")).orElse("0"));
                } catch (Exception ex) {
                    page = 0;
                }
                int limit = 12;
                User user = userService.findById(userId);
                String search = Optional.ofNullable(request.getParameter("search")).orElse("_");
                List<ReportForUserReportTableDTO> reports = reportService.getAllByUserForUserTable(user, search);
                Pagination<ReportForUserReportTableDTO>  pagination = new Pagination<>(limit, reports);
                reports.sort(Comparator
                        .comparing(ReportForUserReportTableDTO::getUpdated)
                        .thenComparing(ReportForUserReportTableDTO::getId)
                        .reversed());
                reports = pagination.getPage(page);
                request.setAttribute("totalPages", pagination.getTotalPages());
                request.setAttribute("reports", reports);
                request.getRequestDispatcher("/WEB-INF/templates/home/userHome.jsp").forward(request, response);
            } catch (UsernameNotFoundException ex) {
                response.sendError(403);
            }
        } else {
            throw new RuntimeException();
        }
    }
}
