package com.kpi.testing.dao.impl;

import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCDaoFactory extends DaoFactory {
    private Connection connection;
    private String PROP_FILE = "application.properties";
    private final static String DB_URL = "db.url";
    private final static String DB_USER = "db.user";
    private final static String DB_PASSWORD = "db.password";
    private String urlDb;
    private String user;
    private String password;

    @Override
    public UserDAO createUserDao() {
        return new JDBCUserDAO(getConnection());
    }

    @Override
    public ReportDAO createReportDao() {
        return new JDBCReportDAO(getConnection());
    }

    @Override
    public ArchiveDAO createArchiveDao() {
        return new JDBCArchiveDAO(getConnection());
    }

    @Override
    public JDBCSqlExecutor createExecutor() {
        return new JDBCSqlExecutor(getConnection());
    }

    private Connection getConnection() {
        if (connection == null) {
            synchronized (JDBCDaoFactory.class) {
                if (connection == null) {
                    try {
                        getProp();
                        connection = DriverManager.getConnection(urlDb, user, password);
                        connection.prepareStatement("SET @@SESSION.information_schema_stats_expiry = 0;").executeQuery();
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return connection;
    }


    private void getProp() throws IOException{
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROP_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            urlDb = properties.getProperty(DB_URL);
            user = properties.getProperty(DB_USER);
            password = properties.getProperty(DB_PASSWORD);
        }
    }

    @Override
    public void setProp(String prop) {
        if (!PROP_FILE.equals(prop)) {
            this.PROP_FILE = prop;
            connection = null;
            getConnection();
        }
    }
}



