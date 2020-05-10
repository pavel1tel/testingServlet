package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.impl.JDBCDaoFactory;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
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

import static org.mockito.Mockito.*;

public class IndexPageTest {
    final Servlet servlet = new Servlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private HttpSession session;

    private final JDBCSqlExecutor executor;


    public IndexPageTest() throws ServletException {
        MockitoAnnotations.initMocks(this);
        DaoFactory factory = DaoFactory.getInstance();
        factory.setProp("testDb.properties");
        executor = factory.createExecutor();
        servlet.init();
        when(request.getSession()).thenReturn(session);
    }

    @BeforeEach
    public void createDb() throws FileNotFoundException {
        executor.executeSql("src/test/resources/sql/create_user_before.sql");
        executor.executeSql("src/test/resources/sql/create_report_before.sql");
    }

    @AfterEach
    public void clearDb() throws FileNotFoundException {
        executor.executeSql("src/test/resources/sql/after.sql");
    }

    @Test
    public void doGetOnIndexReturnIndexPageAnon() throws ServletException, IOException {

        when(request.getRequestURI()).thenReturn("/app/index");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher("/index.jsp");
        verify(dispatcher).forward(request, response);

    }

    @Test
    public void doGetOnIndexReturnIndexPageUser() throws ServletException, IOException {

        when(request.getRequestURI()).thenReturn("/app/index");
        when(request.getContextPath()).thenReturn("/tax");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request.getSession(), times(1)).setAttribute("homeUrl", request.getContextPath() + "/app/userHome");
        verify(request.getSession(), never()).setAttribute("homeUrl", request.getContextPath() + "/app/inspHome");
        verify(request, times(1)).getRequestDispatcher("/index.jsp");
        verify(dispatcher).forward(request, response);

    }

    @Test
    public void doGetOnIndexReturnIndexPageInspector() throws ServletException, IOException {

        when(request.getRequestURI()).thenReturn("/app/index");
        when(request.getContextPath()).thenReturn("/tax");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("3");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request.getSession(), never()).setAttribute("homeUrl", request.getContextPath() + "/app/userHome");
        verify(request.getSession(), times(1)).setAttribute("homeUrl", request.getContextPath() + "/app/inspHome");
        verify(request, times(1)).getRequestDispatcher("/index.jsp");
        verify(dispatcher).forward(request, response);

    }
}
