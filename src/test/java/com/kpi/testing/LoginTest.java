package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.controller.filters.AuthFilter;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.impl.DataSourceHolder;
import com.kpi.testing.dao.impl.JDBCDaoFactory;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class LoginTest {
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

    private final DaoFactory factory;

    private final AuthFilter authFilter = new AuthFilter();

    public LoginTest() throws ServletException {
        MockitoAnnotations.initMocks(this);
        DataSourceHolder dataSourceHolder = new DataSourceHolder();
        dataSourceHolder.setProp("testDb.properties");
        factory = DaoFactory.getInstance();
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
    public void getLoginPageAnon() throws IOException, ServletException {

        when(request.getRequestURI()).thenReturn("/app/accounts/login");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getRequestDispatcher("/WEB-INF/templates/accounts/login.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/accounts/login.jsp");
        verify(dispatcher).forward(request, response);

    }


    @Test
    public void postLogin() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/app/accounts/login");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getSession().getAttribute("user")).thenReturn(null);
        when(request.getParameter("email")).thenReturn("user@gmail.com");
        when(request.getParameter("password")).thenReturn("grib1111");
        when(request.getContextPath()).thenReturn("/tax");
        when(request.getRequestDispatcher("/WEB-INF/templates/accounts/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(response, times(1)).sendRedirect("/tax/app/index");
    }
}
