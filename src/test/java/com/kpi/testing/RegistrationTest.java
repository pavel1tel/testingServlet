package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.dao.impl.DataSourceHolder;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UsernameNotFoundException;
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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class RegistrationTest {
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
    @Mock
    private UserDAO userDAO;

    private final UserService userService;

    private final JDBCSqlExecutor executor;


    public RegistrationTest() throws ServletException {
        MockitoAnnotations.initMocks(this);
        DataSourceHolder dataSourceHolder = new DataSourceHolder();
        dataSourceHolder.setProp("testDb.properties");
        DaoFactory factory = DaoFactory.getInstance();
        userService = new UserService();
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
    public void getRegistrationPage() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/app/accounts/registration");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getRequestDispatcher("/WEB-INF/templates/accounts/registration.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher("/WEB-INF/templates/accounts/registration.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void  postRegistration() throws IOException, ServletException, UsernameNotFoundException {
        when(request.getRequestURI()).thenReturn("/app/accounts/registration");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getRequestDispatcher("/WEB-INF/templates/accounts/registration.jsp")).thenReturn(dispatcher);
        when(request.getParameter("email")).thenReturn("test@gmail.com");
        when(request.getParameter("password")).thenReturn("test12345");
        when(request.getParameter("confirmPassword")).thenReturn("test12345");
        when(request.getParameter("username")).thenReturn("test");
        when(request.getContextPath()).thenReturn("/tax");

        servlet.doPost(request, response);

        Assert.assertEquals("test", userService.loadUserByEmail("test@gmail.com").getUsername());

        verify(response, times(1)).sendRedirect("/tax/app/accounts/login");
    }

    @Test
    public void notValidRegistration() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/app/accounts/registration");
        when(request.getSession().getAttribute("loggedIn")).thenReturn("false");
        when(request.getRequestDispatcher("/WEB-INF/templates/accounts/registration.jsp")).thenReturn(dispatcher);
        when(request.getParameter("email")).thenReturn("test@gmail.com");
        when(request.getParameter("password")).thenReturn("test12345");
        when(request.getParameter("confirmPassword")).thenReturn("test123");
        when(request.getParameter("username")).thenReturn("test");
        when(request.getContextPath()).thenReturn("/tax");

        servlet.doPost(request, response);

        verify(response, times(1)).sendRedirect("/tax/app/accounts/registration?error=true");
    }
}
