package com.kpi.testing.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

public class JDBCSqlExecutor {
    private final Connection connection;

    public JDBCSqlExecutor(Connection connection) {
        this.connection = connection;
    }

    public void executeSql(String path) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(connection);
        Reader reader = new BufferedReader(new FileReader(path));
        sr.setLogWriter(null);
        sr.runScript(reader);
    }
}
