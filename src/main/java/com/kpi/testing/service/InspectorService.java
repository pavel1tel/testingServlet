package com.kpi.testing.service;

import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.dto.DeclineReasonDTO;
import com.kpi.testing.entity.Archive;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.util.SinglModelMapper;
import com.kpi.testing.util.annotation.Transactional;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class InspectorService {
    UserDAO userDAO;
    ReportDAO reportDAO;
    ArchiveDAO archiveDAO;
    ModelMapper modelMapper;

    public InspectorService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDAO = factory.createUserDao();
        reportDAO = factory.createReportDao();
        archiveDAO = factory.createArchiveDao();
        modelMapper = SinglModelMapper.getInstance().getMapper();
    }

    @Transactional
    public void declineReport(Report reportToDecline, DeclineReasonDTO reportReason, User inspector) throws SQLException {
        Archive archive = Archive.builder()
                .report(reportToDecline)
                .inspectorDecision(inspector)
                .name(reportToDecline.getName())
                .description(reportToDecline.getDescription())
                .declineReason(reportReason.getDeclineReason())
                .status(ReportStatus.NOT_ACCEPTED)
                .build();
        archiveDAO.create(archive);
        reportToDecline.setDeclineReason(reportReason.getDeclineReason());
        reportToDecline.setStatus(ReportStatus.NOT_ACCEPTED);
        reportDAO.update(reportToDecline);

    }

    @Transactional
    public void acceptReport(Report reportToDecline, User inspector) throws SQLException {
        //todo dont work
        Archive archive = Archive.builder()
                .report(reportToDecline)
                .inspectorDecision(inspector)
                .name(reportToDecline.getName())
                .description(reportToDecline.getDescription())
                .status(ReportStatus.ACCEPTED)
                .build();
        archiveDAO.create(archive);
        reportToDecline.setStatus(ReportStatus.ACCEPTED);
        reportToDecline.setInspectors(new ArrayList<>());
        reportDAO.update(reportToDecline);
    }
}
