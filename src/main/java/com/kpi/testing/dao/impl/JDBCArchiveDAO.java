package com.kpi.testing.dao.impl;

import com.kpi.testing.dao.ArchiveDAO;
import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.entity.Archive;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.ReportStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCArchiveDAO implements ArchiveDAO {

    Connection connection;

    public JDBCArchiveDAO(Connection connection) {
        this.connection = connection;
    }

    public static Archive extractArchive(ResultSet rs) throws SQLException {
        try {
            return Archive.builder()
                    .id(rs.getLong("archive.id"))
                    .created(rs.getDate("archive.created").toLocalDate())
                    .updated(rs.getDate("archive.updated").toLocalDate())
                    .declineReason(rs.getString("archive.decline_reason"))
                    .description(rs.getString("archive.description"))
                    .name(rs.getString("archive.name"))
                    .build();
        } catch (NullPointerException ignored) {
            return new Archive();
        }
    }

    public static void main(String[] args) {
        Report report = Report.builder().id(21L).build();
        User user = User.builder().id(4L).build();
        Archive archive = Archive.builder()
                .id(5L)
                .declineReason("up")
                .description("upd")
                .name("upn")
                .inspectorDecision(user)
                .report(report).build();
        DaoFactory factory = new JDBCDaoFactory();
        ArchiveDAO archiveDAO = factory.createArchiveDao();
        archiveDAO.update(archive);
    }

    @Override
    public  Optional<Archive> findLastByReport(Report report) {
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM archive" +
                " left join usr on archive.inspector_decision_id = usr.id" +
                " where report_id = ? order by archive.id desc" +
                " limit 1")){
            ps.setLong(1, report.getId());
            ResultSet rs = ps.executeQuery();
            Optional<Archive> archive = Optional.empty();
            while (rs.next()) {
                User inspector = JDBCUserDAO.extractUser(rs);
                archive = Optional.of(extractArchive(rs));
                archive.get().setInspectorDecision(inspector);
                archive.get().setReport(report);
            }
            return archive;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void create(Archive entity) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO archive" +
                "(inspector_decision_id, report_id, description, name, decline_reason, created, updated)" +
                " values(?, ?, ?, ?, ?, ?, ?)")) {
            ps.setLong(1, entity.getInspectorDecision().getId());
            ps.setLong(2, entity.getReport().getId());
            ps.setString(3, entity.getDescription());
            ps.setString(4, entity.getName());
            ps.setString(5, entity.getDeclineReason());
            ps.setString(6, LocalDate.now().toString());
            ps.setString(7, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException exeption) {
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Archive> findById(Long id) {
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM archive" +
                " left join usr on archive.inspector_decision_id = usr.id" +
                " left join reports on archive.report_id = reports.id" +
                " where archive.id = ?")){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Optional<Archive> archive = Optional.empty();
            while (rs.next()) {
                User inspector = JDBCUserDAO.extractUser(rs);
                Report report = JDBCReportDAO.extractReport(rs);
                archive = Optional.of(extractArchive(rs));
                archive.get().setInspectorDecision(inspector);
                archive.get().setReport(report);
            }
            return archive;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Archive> findAll() {
        List<Archive> result = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM archive" +
                " left join usr on archive.inspector_decision_id = usr.id" +
                " left join reports on archive.report_id = reports.id")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User inspector = JDBCUserDAO.extractUser(rs);
                Report report = JDBCReportDAO.extractReport(rs);
                Archive archive = extractArchive(rs);
                archive.setInspectorDecision(inspector);
                archive.setReport(report);
                result.add(archive);
            }
            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Archive entity) {
        try (PreparedStatement ps = connection.prepareStatement
                ("Update archive set name = ?, description = ?, decline_reason = ?, inspector_decision_id = ?," +
                        " report_id = ?, updated = ?" +
                        "where id = ?")) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, entity.getDeclineReason());
            ps.setLong(4, entity.getInspectorDecision().getId());
            ps.setLong(5, entity.getReport().getId());
            ps.setString(6, LocalDate.now().toString());
            ps.setLong(7, entity.getId());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(Long id) {
        try(PreparedStatement ps = connection.prepareStatement("DELETE FROM archive WHERE id = ?")){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
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
