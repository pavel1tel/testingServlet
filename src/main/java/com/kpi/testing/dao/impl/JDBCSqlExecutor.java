package com.kpi.testing.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCSqlExecutor {
    private final DataSource ds;

    public JDBCSqlExecutor(DataSource ds) {
        this.ds = ds;
    }

    public void executeSql(String path) throws FileNotFoundException {
        try (Connection connection = ds.getConnection()) {

            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(path));
            sr.setLogWriter(null);
            sr.runScript(reader);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
