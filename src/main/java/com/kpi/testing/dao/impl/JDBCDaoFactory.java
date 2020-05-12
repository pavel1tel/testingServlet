package com.kpi.testing.dao.impl;

import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;

import javax.sql.DataSource;
import java.io.*;

public class JDBCDaoFactory extends DaoFactory {


    @Override
    public UserDAO createUserDao() {
        return new JDBCUserDAO(getDataSource());
    }

    @Override
    public ReportDAO createReportDao() {
        return new JDBCReportDAO(getDataSource());
    }

    @Override
    public ArchiveDAO createArchiveDao() {
        return new JDBCArchiveDAO(getDataSource());
    }

    @Override
    public JDBCSqlExecutor createExecutor() {
        return new JDBCSqlExecutor(getDataSource());
    }

    private DataSource getDataSource() {
        try {
            return DataSourceHolder.getDataSource();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



