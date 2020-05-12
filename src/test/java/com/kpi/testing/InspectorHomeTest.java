package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.impl.DataSourceHolder;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
import com.kpi.testing.dto.ReportForInspectorReportTableDTO;
import com.kpi.testing.entity.Archive;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;

public class InspectorHomeTest {
    final Servlet servlet = new Servlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private HttpSession session;

    private final ReportService reportService;
    private final UserService userService;
    private final ArchiveDAO archiveDAO;

    private final JDBCSqlExecutor executor;

    public InspectorHomeTest() throws ServletException {
        MockitoAnnotations.initMocks(this);
        DataSourceHolder dataSourceHolder = new DataSourceHolder();
        dataSourceHolder.setProp("testDb.properties");
        DaoFactory factory = DaoFactory.getInstance();
        archiveDAO = factory.createArchiveDao();
        reportService = new ReportService();
        userService = new UserService();
        executor = factory.createExecutor();
        servlet.init();
        when(request.getSession()).thenReturn(session);
    }

    @BeforeEach
    public void createDb() throws FileNotFoundException {
        executor.executeSql("src/test/resources/sql/create_user_before.sql");
        executor.executeSql("src/test/resources/sql/create_report_before.sql");
        executor.executeSql("src/test/resources/sql/create_archive_before.sql");
    }

    @AfterEach
    public void clearDb() throws FileNotFoundException {
        executor.executeSql("src/test/resources/sql/after.sql");
    }

    @Test
    public void getInspHome() throws ServletException, IOException, UsernameNotFoundException {
        when(request.getRequestURI()).thenReturn("/app/inspHome");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("2");
        when(request.getRequestDispatcher("/WEB-INF/templates/home/inspHome.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        User user = userService.findById(2L);
        List<ReportForInspectorReportTableDTO> reports = reportService.getAllByInspectorAndStatusForTable(user, ReportStatus.QUEUE, "_");
        reports.sort(Comparator.comparing(ReportForInspectorReportTableDTO::getId));

        verify(request, times(1)).setAttribute("totalPages", 1);
        verify(request, times(1)).setAttribute("reports", reports);
        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/home/inspHome.jsp");
    }

    @Test
    public void getDeclineReasonAdd() throws ServletException, IOException, UsernameNotFoundException {
        when(request.getRequestURI()).thenReturn("/app/inspHome/decline/1");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("2");
        when(request.getRequestDispatcher("/WEB-INF/templates/home/addReason.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/home/addReason.jsp");
    }

    @Test
    public void postDeclineReport() throws ServletException, IOException, UnknownReportError {
        when(request.getRequestURI()).thenReturn("/app/inspHome/decline/1");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("2");
        when(request.getContextPath()).thenReturn("/tax");

        servlet.doPost(request, response);

        Report report = reportService.getById(1L);
        Archive archive = archiveDAO.findLastByReport(report).orElse(new Archive());

        Assert.assertEquals(ReportStatus.NOT_ACCEPTED, report.getStatus());
        Assert.assertEquals(ReportStatus.NOT_ACCEPTED, archive.getStatus());
        verify(response, times(1)).sendRedirect("/tax/app/inspHome");
    }

    @Test
    public void postAcceptReport() throws ServletException, IOException, UnknownReportError {
        when(request.getRequestURI()).thenReturn("/app/inspHome/accept/1");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("2");
        when(request.getContextPath()).thenReturn("/tax");

        servlet.doPost(request, response);

        Report report = reportService.getById(1L);
        Archive archive = archiveDAO.findLastByReport(report).orElse(new Archive());

        Assert.assertEquals(ReportStatus.ACCEPTED, report.getStatus());
        Assert.assertEquals(ReportStatus.ACCEPTED, archive.getStatus());
        verify(response, times(1)).sendRedirect("/tax/app/inspHome");
    }
}
