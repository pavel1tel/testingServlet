package com.kpi.testing.dao.impl;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceHolder {
    private static String PROP_FILE = "application.properties";
    private final static String DB_URL = "db.url";
    private final static String DB_USER = "db.user";
    private final static String DB_PASSWORD = "db.password";

    private static volatile BasicDataSource dataSource;

    public static DataSource getDataSource() throws IOException {
        if (dataSource == null){
            synchronized (DataSourceHolder.class) {
                if (dataSource == null) {
                    dataSource = new BasicDataSource();
                    setProp(dataSource);
                }
            }
        }
        return dataSource;
    }

    private static void setProp(BasicDataSource ds) throws IOException {
        try (InputStream input = DataSourceHolder.class.getClassLoader().getResourceAsStream(PROP_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            ds.setUrl(properties.getProperty(DB_URL));
            ds.setUsername(properties.getProperty(DB_USER));
            ds.setPassword(properties.getProperty(DB_PASSWORD));
            ds.setMaxIdle(30);
            ds.setMaxOpenPreparedStatements(80);
        }
    }

    public void setProp(String prop) {
        if (!PROP_FILE.equals(prop)) {
            PROP_FILE = prop;
            dataSource = null;
            try {
                getDataSource();
            } catch (IOException ignored) {
            }
        }
    }
}
