package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.dto.UpdateReportDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.service.ReportOwnerService;
import com.kpi.testing.service.ReportService;
import com.kpi.testing.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
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

public class UserHomeTest {
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

    private final JDBCSqlExecutor executor;


    public UserHomeTest() throws ServletException {
        MockitoAnnotations.initMocks(this);
        DaoFactory factory = DaoFactory.getInstance();
        factory.setProp("testDb.properties");
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
    public void getUserHome() throws ServletException, IOException, UsernameNotFoundException {
        when(request.getRequestURI()).thenReturn("/app/userHome");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/templates/home/userHome.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);
        User user = userService.findById(1L);
        List<ReportForUserReportTableDTO> reports = reportService.getAllByUserForUserTable(user, "_");
        reports.sort(Comparator
                .comparing(ReportForUserReportTableDTO::getUpdated)
                .thenComparing(ReportForUserReportTableDTO::getId)
                .reversed());
        verify(request, times(1)).setAttribute("totalPages", 1);
        verify(request, times(1)).setAttribute("reports", reports);
        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/home/userHome.jsp");
    }

    @Test
    public void getAddReport() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/app/userHome/add");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/templates/home/addReport.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/home/addReport.jsp");
    }

    @Test
    public void getUpdateReport() throws ServletException, IOException, UnknownReportError {
        when(request.getRequestURI()).thenReturn("/app/userHome/update/3");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/templates/home/updateReport.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);
        UpdateReportDTO report = reportService.getForUpdate(3L);
        verify(request, times(1)).setAttribute("report", report);
        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/home/updateReport.jsp");
    }

    @Test
    public void postUpdateReport() throws ServletException, IOException, UnknownReportError {
        when(request.getRequestURI()).thenReturn("/app/userHome/update/3");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/templates/home/updateReport.jsp")).thenReturn(dispatcher);
        when(request.getParameter("name")).thenReturn("updated");
        when(request.getParameter("description")).thenReturn("updated description");
        when(request.getContextPath()).thenReturn("/tax");

        servlet.doPost(request, response);

        Report updated = reportService.getById(3L);
        Assert.assertEquals("updated", updated.getName());
        Assert.assertEquals("updated description", updated.getDescription());

        verify(response, times(1)).sendRedirect("/tax/app/userHome");
    }

    @Test
    public void postChangeInspector() throws IOException, UnknownReportError, ServletException {
        when(request.getRequestURI()).thenReturn("/app/userHome/change/3");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getContextPath()).thenReturn("/tax");

        servlet.doPost(request, response);

        Report updated = reportService.getById(3L);

        Assert.assertEquals(1, updated.getInspectors().size());
        verify(response, times(1)).sendRedirect("/tax/app/userHome");
    }
}
