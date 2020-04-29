package com.kpi.testing.service;

import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.dto.ReportDTO;
import com.kpi.testing.dto.ReportForInspectorReportTableDTO;
import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;
import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.util.SinglModelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportService {
    UserDAO userDAO;
    ReportDAO reportDAO;
    ModelMapper modelMapper;

    public ReportService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDAO = factory.createUserDao();
        reportDAO = factory.createReportDao();
        modelMapper = SinglModelMapper.getInstance().getMapper();
    }

    public List<ReportForUserReportTableDTO> getAllByUserForUserTable(User user) {
        List<Report> reports = reportDAO.findByOwner(user);
        Type pageType = new TypeToken<List<ReportForUserReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }

    public List<ReportForInspectorReportTableDTO> getAllByInspectorAndStatusForTable(User user, ReportStatus status) {
        List<Report> reports = reportDAO.findAllByInspectorsAndStatus(user, status);
        Type pageType = new TypeToken<List<ReportForInspectorReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }

    public Report getFromDTO(ReportDTO reportDTO) {
        return modelMapper.map(reportDTO, Report.class);
    }

    public void save(ReportDTO reportDTO, User owner) {
        Report report = getFromDTO(reportDTO);
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
        int listSizeIndex = 2;
        return list.subList(0, listSizeIndex);
    }
}
