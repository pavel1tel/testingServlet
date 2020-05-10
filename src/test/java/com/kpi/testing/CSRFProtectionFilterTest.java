package com.kpi.testing;

import com.kpi.testing.controller.Servlet;
import com.kpi.testing.controller.filters.CSRFFilter;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class CSRFProtectionFilterTest {
    final Servlet servlet = new Servlet();
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private final JDBCSqlExecutor executor;

    public CSRFProtectionFilterTest() throws ServletException {
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
        executor.executeSql("src/test/resources/sql/create_archive_before.sql");
    }

    @AfterEach
    public void clearDb() throws FileNotFoundException {
        executor.executeSql("src/test/resources/sql/after.sql");
    }

    @Test
    public void postWithoutToken() throws IOException, ServletException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContextPath()).thenReturn("/tax");
        when(request.getSession().getAttribute("csrfToken")).thenReturn("test");

        CSRFFilter csrfFilter = new CSRFFilter();
        csrfFilter.doFilter(request, response, filterChain);

        verify(response, times(1)).sendRedirect("/tax/app/error");
    }

    @Test
    public void postWithInvalidToken() throws IOException, ServletException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContextPath()).thenReturn("/tax");
        when(request.getSession().getAttribute("csrfToken")).thenReturn("test");
        when(request.getParameter("csrfToken")).thenReturn("test2");

        CSRFFilter csrfFilter = new CSRFFilter();
        csrfFilter.doFilter(request, response, filterChain);

        verify(response, times(1)).sendRedirect("/tax/app/error");
    }

    @Test
    public void postWithValidToken() throws IOException, ServletException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContextPath()).thenReturn("/tax");
        when(request.getSession().getAttribute("csrfToken")).thenReturn("test");
        when(request.getParameter("csrfToken")).thenReturn("test");

        CSRFFilter csrfFilter = new CSRFFilter();
        csrfFilter.doFilter(request, response, filterChain);

        verify(response, never()).sendRedirect("/tax/app/error");
    }
}
