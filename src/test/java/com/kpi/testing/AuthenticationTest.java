package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.controller.filters.AuthFilter;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.impl.DataSourceHolder;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
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

import static org.mockito.Mockito.*;

public class AuthenticationTest {
    final Servlet servlet = new Servlet();
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private HttpSession session;

    private final JDBCSqlExecutor executor;

    public AuthenticationTest() throws ServletException {
        MockitoAnnotations.initMocks(this);
        DataSourceHolder dataSourceHolder = new DataSourceHolder();
        dataSourceHolder.setProp("testDb.properties");
        DaoFactory factory = DaoFactory.getInstance();
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
    public void CommonPages() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/app/index");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");

        AuthFilter authFilter = new AuthFilter();
        authFilter.doFilter(request, response, filterChain);

        verify(request, never()).getRequestDispatcher("/WEB-INF/templates/denied.jsp");

        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestURI()).thenReturn("/app/accounts/logout");

        authFilter.doFilter(request, response, filterChain);

        verify(request, never()).getRequestDispatcher("/WEB-INF/templates/denied.jsp");
    }

    @Test
    public void GuestPages() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/app/accounts/login");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getRequestDispatcher("/WEB-INF/templates/denied.jsp")).thenReturn(dispatcher);

        AuthFilter authFilter = new AuthFilter();
        authFilter.doFilter(request, response, filterChain);

        verify(request, never()).getRequestDispatcher("/WEB-INF/templates/denied.jsp");

        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");

        authFilter.doFilter(request, response, filterChain);

        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/denied.jsp");
    }

    @Test
    public void UserPages() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/app/userHome");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/templates/denied.jsp")).thenReturn(dispatcher);

        AuthFilter authFilter = new AuthFilter();
        authFilter.doFilter(request, response, filterChain);

        verify(request, never()).getRequestDispatcher("/WEB-INF/templates/denied.jsp");

        when(request.getSession().getAttribute("user")).thenReturn("2");

        authFilter.doFilter(request, response, filterChain);

        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/denied.jsp");
    }

    @Test
    public void InspectorPages() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/app/inspHome");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("true");
        when(request.getSession().getAttribute("user")).thenReturn("2");
        when(request.getRequestDispatcher("/WEB-INF/templates/denied.jsp")).thenReturn(dispatcher);

        AuthFilter authFilter = new AuthFilter();
        authFilter.doFilter(request, response, filterChain);

        verify(request, never()).getRequestDispatcher("/WEB-INF/templates/denied.jsp");

        when(request.getSession().getAttribute("user")).thenReturn("1");

        authFilter.doFilter(request, response, filterChain);

        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/denied.jsp");
    }
}
