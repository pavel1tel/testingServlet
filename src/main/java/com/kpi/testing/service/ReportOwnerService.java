package com.kpi.testing.service;

import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.util.SinglModelMapper;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReportOwnerService {
    UserDAO userDAO;
    ReportDAO reportDAO;
    ArchiveDAO archiveDAO;
    ModelMapper modelMapper;

    public ReportOwnerService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDAO = factory.createUserDao();
        reportDAO = factory.createReportDao();
        archiveDAO = factory.createArchiveDao();
        modelMapper = SinglModelMapper.getInstance().getMapper();
    }

    public void changeInspector(Report report) {
        List<User> inspectors = report.getInspectors();
        if (inspectors.size() <= 1) {
            report.setStatus(ReportStatus.QUEUE);
            reportDAO.update(report);
            return;
        }
        Long inspectorId = archiveDAO.findLastByReport(report).orElseThrow(RuntimeException::new).getInspectorDecision().getId();
        List<User> newInspectors = inspectors.stream()
                .filter(inspector -> !inspector.getId().equals(inspectorId))
                .collect(Collectors.toList());
        report.setStatus(ReportStatus.QUEUE);
        report.setInspectors(newInspectors);
        reportDAO.update(report);
    }
}
