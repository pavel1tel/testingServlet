package com.kpi.testing.dao;

import com.kpi.testing.entity.Archive;
import com.kpi.testing.entity.Report;

import java.util.Optional;

public interface ArchiveDAO extends BaseDAO<Archive> {
    public Optional<Archive> findLastByReport(Report report);
}
