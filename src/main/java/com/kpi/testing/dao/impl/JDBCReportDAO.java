package com.kpi.testing.dao.impl;

import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.ReportDAO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.kpi.testing.dao.impl.JDBCUserDAO.extractUser;

public class JDBCReportDAO implements ReportDAO {
    Connection connection;

    public JDBCReportDAO(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
        DaoFactory factory = JDBCDaoFactory.getInstance();
        ReportDAO reportDAO = factory.createReportDao();
        Report report = Report.builder()
                .created(LocalDate.now())
                .updated(LocalDate.now())
                .name("itWorks!")
                .description("itWorks!")
                .declineReason(null)
                .status(ReportStatus.NOT_ACCEPTED)
                .owner(User.builder().id(2L).build())
                .inspectors(new ArrayList<User>(Arrays.asList(User.builder().id(2L).build())))
                .build();
        System.out.println(reportDAO.findById(12L));
    }

    public static Report extractReport(ResultSet rs) throws SQLException {
        try {
            return Report.builder()
                    .id(rs.getLong("reports.id"))
                    .created(rs.getDate("reports.created").toLocalDate())
                    .updated(rs.getDate("reports.updated").toLocalDate())
                    .status(ReportStatus.valueOf(rs.getString("reports.status")))
                    .declineReason(rs.getString("decline_reason"))
                    .description(rs.getString("description"))
                    .name(rs.getString("name"))
                    .build();
        } catch (NullPointerException ignored) {
            return new Report();
        }
    }

    private boolean isUniqUser(Map<Long, User> users, User user) {
        return !users.containsKey(user.getId());
    }

    private Report makeUniqueReport(Map<Long, Report> reports, Report report) {
        reports.putIfAbsent(report.getId(), report);
        return reports.get(report.getId());
    }

    @Override
    public List<Report> findByOwner(User user) {
        List<Report> result;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("select * from reports" +
                            " left join report_inspectors" +
                            " on reports.id = report_inspectors.report_id" +
                            " left join usr on usr.id = usr_id" +
                            " where owner_id = %d", user.getId())
            );
            Map<Long, User> inspectors = new HashMap<>();
            Map<Long, Report> reports = new HashMap<>();
            while (rs1.next()) {
                Report report = makeUniqueReport(reports, extractReport(rs1));

                User inspector = extractUser(rs1);
                if (isUniqUser(inspectors, inspector)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    inspectors.putIfAbsent(report.getId(), inspector);
                    report.getInspectors().add(inspector);
                }
            }
            result = new ArrayList<>(reports.values());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public List<Report> findAllByInspectorsAndStatus(User inspector, ReportStatus status) {
        List<Report> result;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("select * from report_inspectors" +
                            " left join reports" +
                            " on report_inspectors.report_id = reports.id" +
                            " where usr_id = %d and reports.status = '%s'", inspector.getId(), status.name())
            );
            Map<Long, Report> reports = new HashMap<>();
            while (rs1.next()) {
                makeUniqueReport(reports, extractReport(rs1));
            }
            result = new ArrayList<>(reports.values());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public void create(Report entity) {
        int parameters = entity.getInspectors().size();
        int id;
        try (PreparedStatement ps = connection.prepareStatement
                ("insert into reports (`status`, `updated`, `name`, `description`, `decline_reason`, `owner_id`, `created`) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)")) {
            connection.setAutoCommit(false);
            ps.setString(1, entity.getStatus().name());
            ps.setString(2, LocalDate.now().toString());
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getDeclineReason());
            ps.setLong(6, entity.getOwner().getId());
            ps.setString(7, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                throw new RuntimeException();
            }
            throw new RuntimeException();
        }
        if (parameters > 0) {
            StringBuffer query = new StringBuffer("insert into report_inspectors (usr_id, report_id) values ");
            if (parameters > 1) {
                IntStream.range(0, parameters - 1).forEachOrdered(ignored -> query.append("(?, ?), "));
            }
            query.append("(?, ?);");
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT `AUTO_INCREMENT` " +
                        "FROM  INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = 'testing' " +
                        "AND   TABLE_NAME   = 'reports';");
                ResultSet rs = ps.executeQuery();
                rs.next();
                id = rs.getInt("AUTO_INCREMENT") - 1;
            } catch (SQLException ignored) {
                throw new RuntimeException();
            }
            try {
                PreparedStatement ps = connection.prepareStatement(query.toString());
                AtomicInteger counter = new AtomicInteger(1);
                IntStream.range(0, parameters).forEachOrdered(i -> {
                    try {
                        ps.setLong(counter.getAndIncrement(), entity.getInspectors().get(i).getId());
                        ps.setLong(counter.getAndIncrement(), id);
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
                ps.executeUpdate();
                ps.close();
                connection.commit();
            } catch (SQLException exception) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                    throw new RuntimeException();
                }
                exception.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    @Override
    public Optional<Report> findById(Long id) {
        Optional<Report> report;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("select * from reports" +
                            " left join report_inspectors" +
                            " on reports.id = report_inspectors.report_id" +
                            " left join usr on usr.id = usr_id" +
                            " where reports.id = %d", id)
            );
            report = (rs1.next()) ? Optional.of(extractReport(rs1)) : Optional.empty();
            rs1.previous();
            Map<Long, User> inspectors = new HashMap<>();
            while (rs1.next()) {
                User inspector = extractUser(rs1);
                if (isUniqUser(inspectors, inspector)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    inspectors.putIfAbsent(report.orElse(new Report()).getId(), inspector);
                    report.orElse(new Report()).getInspectors().add(inspector);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return report;
    }

    @Override
    public List<Report> findAll() {
        List<Report> result;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    "select * from reports" +
                            " left join report_inspectors" +
                            " on reports.id = report_inspectors.report_id" +
                            " left join usr on usr.id = usr_id"
            );
            Map<Long, User> inspectors = new HashMap<>();
            Map<Long, Report> reports = new HashMap<>();
            while (rs1.next()) {
                Report report = makeUniqueReport(reports, extractReport(rs1));
                User inspector = extractUser(rs1);
                if (isUniqUser(inspectors, inspector)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    inspectors.putIfAbsent(report.getId(), inspector);
                    report.getInspectors().add(inspector);
                }
            }
            result = new ArrayList<>(reports.values());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public void update(Report entity) {
        int parameters = entity.getInspectors().size();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("delete from report_inspectors where report_id = ?");
            ps.setLong(1, entity.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ignored){
            throw new RuntimeException();
        }
        if (parameters > 0) {
            StringBuffer query = new StringBuffer("insert into report_inspectors (usr_id, report_id) values ");
            if (parameters > 1) {
                IntStream.range(0, parameters - 1).forEachOrdered(ignored -> query.append("(?, ?), "));
            }
            query.append("(?, ?);");
            try {
                PreparedStatement ps = connection.prepareStatement(query.toString());
                AtomicInteger counter = new AtomicInteger(1);
                IntStream.range(0, parameters).forEachOrdered(i -> {
                    try {
                        ps.setLong(counter.getAndIncrement(), entity.getInspectors().get(i).getId());
                        ps.setLong(counter.getAndIncrement(), entity.getId());
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
                ps.executeUpdate();
                ps.close();
            } catch (SQLException exception) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                    throw new RuntimeException();
                }
                throw new RuntimeException();
            }
        }
        try (PreparedStatement ps = connection.prepareStatement
                ("Update reports set status = ?, updated = ?, name = ?, description = ?, decline_reason = ?" +
                        "where id = ?")) {
            ps.setString(1, entity.getStatus().name());
            ps.setString(2, LocalDate.now().toString());
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getDeclineReason());
            //todo owner is always null when getting from db!!!!!!!!!!
            //ps.setLong(6, entity.getOwner().getId());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
            ps.close();
            connection.commit();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                throw new RuntimeException();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(Long id) {
        try(PreparedStatement ps = connection.prepareStatement("delete from report_inspectors where report_id = ?")){
            connection.setAutoCommit(false);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException ignored){
                throw new RuntimeException();
            }
            throw new RuntimeException();
        }
        try(PreparedStatement ps = connection.prepareStatement("delete from reports where id = ?")){
            ps.setLong(1, id);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException ignored){
                throw new RuntimeException();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }
}
