package com.kpi.testing.service;

import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.dto.ReportForUserReportTableDTO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.util.SinglModelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    UserDAO userDao;
    ReportDAO reportDAO;
    ModelMapper modelMapper;

    public ReportService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDao = factory.createUserDao();
        reportDAO = factory.createReportDao();
        modelMapper = SinglModelMapper.getInstance().getMapper();
    }

    public List<ReportForUserReportTableDTO> getAllByUserForUserTable(User user) {
        List<Report> reports = reportDAO.findByOwner(user);
        Type pageType = new TypeToken<List<ReportForUserReportTableDTO>>() {}.getType();
        return modelMapper.map(reports, pageType);
    }
}
