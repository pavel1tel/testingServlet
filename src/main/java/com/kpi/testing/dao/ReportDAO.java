package com.kpi.testing.dao;

import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;

import java.util.List;
import java.util.Optional;

public interface ReportDAO extends BaseDAO<Report>{
    List<Report> findByOwner(User user);
    List<Report> findAllByInspectorsAndStatus(User inspector, ReportStatus status);
}
