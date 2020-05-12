package com.kpi.testing.dao;

import com.kpi.testing.dao.impl.JDBCDaoFactory;
import com.kpi.testing.dao.impl.JDBCSqlExecutor;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract UserDAO createUserDao();
    public abstract ReportDAO createReportDao();
    public abstract ArchiveDAO createArchiveDao();
    public abstract JDBCSqlExecutor createExecutor();


    public static DaoFactory getInstance(){
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null){
                    daoFactory = new JDBCDaoFactory();
                }
            }
        }
        return daoFactory;
    }
}
