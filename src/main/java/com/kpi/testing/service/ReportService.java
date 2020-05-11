package com.kpi.testing.service;

import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.dto.*;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.exceptions.UnknownReportError;
import com.kpi.testing.util.SinglModelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ReportService {
    UserDAO userDAO;
    ReportDAO reportDAO;
    ArchiveDAO archiveDAO;
    ModelMapper modelMapper;

    public ReportService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDAO = factory.createUserDao();
        reportDAO = factory.createReportDao();
        archiveDAO = factory.createArchiveDao();
        modelMapper = SinglModelMapper.getInstance().getMapper();
    }

    public List<ReportForUserReportTableDTO> getAllByUserForUserTable(User user, String name) {
        List<Report> reports = reportDAO.findByOwnerWhereNameLike(user, name);
        Type pageType = new TypeToken<List<ReportForUserReportTableDTO>>() {
        }.getType();
        return modelMapper.map(reports, pageType);
    }

    public List<ReportForInspectorReportTableDTO> getAllByInspectorAndStatusForTable(User user, ReportStatus status, String name) {
        List<Report> reports = reportDAO.findAllByInspectorsAndStatusWhereNameLike(user, status, name);
        Type pageType = new TypeToken<List<ReportForInspectorReportTableDTO>>() {
        }.getType();
        return modelMapper.map(reports, pageType);
    }

    public Report getFromDTO(AddReportDTO addReportDTO) {
        return modelMapper.map(addReportDTO, Report.class);
    }

    public void save(AddReportDTO addReportDTO, User owner) {
        Report report = getFromDTO(addReportDTO);
        report.setStatus(ReportStatus.QUEUE);
        report.setOwner(owner);
        List<User> inspectors = getInscpectors();
        report.setInspectors(getRandomElements(inspectors));
        reportDAO.create(report);
    }

    private List<User> getInscpectors() {
        return userDAO.findAllByRole(Role.ROLE_INSPECTOR);
    }

    private static List<User> getRandomElements(List<User> list) {
        Collections.shuffle(list);
        int listSizeIndex = list.size();
        return list.subList(0, listSizeIndex);
    }

    public UpdateReportDTO getForUpdate(Long id) throws UnknownReportError {
        Report report = reportDAO.findById(id).orElseThrow(UnknownReportError::new);
        return modelMapper.map(report, UpdateReportDTO.class);
    }

    public void update(Report report, UpdateReportDTO reportDTO) {
        report.setName(reportDTO.getName());
        report.setDescription(reportDTO.getDescription());
        report.setStatus(ReportStatus.QUEUE);
        reportDAO.update(report);
    }

    public Report getById(Long id) throws UnknownReportError {
        return reportDAO.findById(id).orElseThrow(UnknownReportError::new);
    }
}
