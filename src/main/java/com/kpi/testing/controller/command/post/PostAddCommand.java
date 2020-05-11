package com.kpi.testing.controller.command.post;

import com.kpi.testing.controller.command.Command;
import com.kpi.testing.dto.AddReportDTO;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class PostAddCommand implements Command {

    private final ReportService reportService;
    private final UserService userService;
    private static final Logger logger = getLogger(PostAddCommand.class);


    public PostAddCommand(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String NAME_PARAM = "name";
        String name = request.getParameter(NAME_PARAM);
        String DESC_PARAM = "description";
        String description = request.getParameter(DESC_PARAM);
        AddReportDTO report = new AddReportDTO();
        report.setName(name);
        report.setDescription(description);
        Long userId = Long.parseLong(request.getSession().getAttribute("user").toString());
        try {
            User user = userService.findById(userId);
            reportService.save(report, user);
            response.sendRedirect(request.getContextPath() + "/app" + "/userHome");
        } catch (UsernameNotFoundException ex) {
            logger.error("INVALID user recorded in session");
            response.sendError(403);
        }
    }
}
