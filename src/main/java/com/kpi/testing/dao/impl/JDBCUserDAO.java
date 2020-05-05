package com.kpi.testing.dao.impl;

import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.entity.Report;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.entity.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class JDBCUserDAO implements UserDAO {
    private final Connection connection;

    public JDBCUserDAO(Connection connection) {
        this.connection = connection;
    }

    public static User extractUser(ResultSet rs) throws SQLException {
        try {
            return User.builder()
                    .id(rs.getLong("usr.id"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .created(rs.getDate("usr.created").toLocalDate())
                    .updated(rs.getDate("usr.updated").toLocalDate())
                    .role(Role.valueOf(rs.getString("role")))
                    .status(Status.valueOf(rs.getString("usr.status")))
                    .build();
        } catch (NullPointerException npe){
            return new User();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> user;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("SELECT *  FROM usr " +
                            "left join report_inspectors " +
                            "on usr.id = usr_id " +
                            "left join reports " +
                            "on usr.id = reports.owner_id or reports.id = report_id " +
                            "where username='%s'", username)
            );
            user = getNewUser(rs1);
            Map<Long, Report> reportsOwned = new HashMap<>();
            Map<Long, Report> reportsInspected = new HashMap<>();
            while (rs1.next()) {
                Report report = JDBCReportDAO.extractReport(rs1);
                if (isUniqReport(reportsInspected, report)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    reportsInspected.putIfAbsent(report.getId(), report);
                    user.orElse(new User()).getReportsInspected().add(report);
                }
                if (isUniqReport(reportsOwned, report)
                        && rs1.getLong("owner_id") == rs1.getLong("usr.id")) {
                    reportsOwned.putIfAbsent(report.getId(), report);
                    user.orElse(new User()).getReportsOwned().add(report);
                }
            }

        } catch (SQLException throwables) {

            throw new RuntimeException();
        }
        return user;
    }

    public static void main(String[] args) throws SQLException {
        JDBCDaoFactory factory = new JDBCDaoFactory();
        UserDAO userDAO = factory.createUserDao();
        User user = User.builder().username("updated")
                .email("pawloiwanov@gmail.com")
                .role(Role.ROLE_INSPECTOR)
                .status(Status.Active)
                .password("pass")
                .created(LocalDate.now())
                .id(3L)
                .build();
        System.out.println(userDAO.findById(1L));
    }

    private boolean isUniqReport(Map<Long, Report> reports, Report report) {
        return !reports.containsKey(report.getId());
    }

    private User makeUniqueUser(Map<Long, User> users, User user) {
        users.putIfAbsent(user.getId(), user);
        return users.get(user.getId());
    }


    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("SELECT *  FROM usr " +
                            "left join report_inspectors " +
                            "on usr.id = usr_id " +
                            "left join reports " +
                            "on usr.id = reports.owner_id or reports.id = report_id " +
                            "where email='%s'", email)
            );
            user = getNewUser(rs1);
            Map<Long, Report> reportsOwned = new HashMap<>();
            Map<Long, Report> reportsInspected = new HashMap<>();
            while (rs1.next()) {
                Report report = JDBCReportDAO.extractReport(rs1);
                if (isUniqReport(reportsInspected, report)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    reportsInspected.putIfAbsent(report.getId(), report);
                    user.orElse(new User()).getReportsInspected().add(report);
                }
                if (isUniqReport(reportsOwned, report)
                        && rs1.getLong("owner_id") == rs1.getLong("usr.id")) {
                    reportsOwned.putIfAbsent(report.getId(), report);
                    user.orElse(new User()).getReportsOwned().add(report);
                }
            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return user;
    }

    @Override
    public List<User> findAllByRole(Role role) {
        List<User> result;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("SELECT *  FROM usr " +
                            "left join report_inspectors " +
                            "on usr.id = usr_id " +
                            "left join reports " +
                            "on usr.id = reports.owner_id or reports.id = report_id " +
                            "where usr.role = '%s'", role.name())
            );
            Map<Long, Report> reportsOwned = new HashMap<>();
            Map<Long, User> users = new HashMap<>();
            Map<Long, Report> reportsInspected = new HashMap<>();
            while (rs1.next()) {
                User user = makeUniqueUser(users, extractUser(rs1));
                Report report = JDBCReportDAO.extractReport(rs1);
                if (isUniqReport(reportsInspected, report)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    reportsInspected.putIfAbsent(report.getId(), report);
                    user.getReportsInspected().add(report);
                }
                if (isUniqReport(reportsOwned, report)
                        && rs1.getLong("owner_id") == rs1.getLong("usr.id")) {
                    reportsOwned.putIfAbsent(report.getId(), report);
                    user.getReportsOwned().add(report);
                }
            }
            result = new ArrayList<>(users.values());

        } catch (SQLException throwables) {

            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public void create(User entity) {
        try (PreparedStatement ps = connection.prepareStatement
                ("INSERT INTO usr " +
                        "(`username`, `email`, `password`, `role`, `status`, `updated`, `created`)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getRole().name());
            ps.setString(5, entity.getStatus().name());
            ps.setString(6, LocalDate.now().toString());
            ps.setString(7, LocalDate.now().toString());
            ps.executeUpdate();

        } catch (SQLException exeption) {

            throw new RuntimeException();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    String.format("SELECT *  FROM usr " +
                            "left join report_inspectors " +
                            "on usr.id = usr_id " +
                            "left join reports " +
                            "on usr.id = reports.owner_id or reports.id = report_id " +
                            "where usr.id='%s'", id)
            );
            user = getNewUser(rs1);
            Map<Long, Report> reportsOwned = new HashMap<>();
            Map<Long, Report> reportsInspected = new HashMap<>();
            while (rs1.next()) {
                Report report = JDBCReportDAO.extractReport(rs1);
                if (isUniqReport(reportsInspected, report)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    reportsInspected.putIfAbsent(report.getId(), report);
                    user.orElse(new User()).getReportsInspected().add(report);
                }
                if (isUniqReport(reportsOwned, report)
                        && rs1.getLong("owner_id") == rs1.getLong("usr.id")) {
                    reportsOwned.putIfAbsent(report.getId(), report);
                    user.orElse(new User()).getReportsOwned().add(report);
                }
            }
        } catch (SQLException throwables) {

            throwables.printStackTrace();
            throw new RuntimeException();
        }

        return user;
    }

    private Optional<User> getNewUser(ResultSet rs) throws SQLException {
        Optional<User> user = (rs.next()) ? Optional.of(extractUser(rs)) : Optional.empty();
        rs.previous();
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> result;
        try (Statement ps = connection.createStatement()) {
            ResultSet rs1 = ps.executeQuery(
                    "SELECT *  FROM usr " +
                            "left join report_inspectors " +
                            "on usr.id = usr_id " +
                            "left join reports " +
                            "on usr.id = reports.owner_id or reports.id = report_id "
            );
            Map<Long, Report> reportsOwned = new HashMap<>();
            Map<Long, User> users = new HashMap<>();
            Map<Long, Report> reportsInspected = new HashMap<>();
            while (rs1.next()) {
                User user = makeUniqueUser(users, extractUser(rs1));
                Report report = JDBCReportDAO.extractReport(rs1);
                if (isUniqReport(reportsInspected, report)
                        && rs1.getLong("report_id") == rs1.getLong("reports.id")) {
                    reportsInspected.putIfAbsent(report.getId(), report);
                    user.getReportsInspected().add(report);
                }
                if (isUniqReport(reportsOwned, report)
                        && rs1.getLong("owner_id") == rs1.getLong("usr.id")) {
                    reportsOwned.putIfAbsent(report.getId(), report);
                    user.getReportsOwned().add(report);
                }
            }
            result = new ArrayList<>(users.values());

        } catch (SQLException throwables) {

            throwables.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    @Override
    public void update(User entity) {
        try (PreparedStatement ps = connection.prepareStatement
                ("Update usr set status = ?, username = ?, email = ?, password = ?, role = ?, updated = ?" +
                        "where id = ?")) {
            ps.setString(1, entity.getStatus().name());
            ps.setString(2, entity.getUsername());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getRole().name());
            ps.setString(7, LocalDate.now().toString());
            ps.setLong(8, entity.getId());
            ps.executeUpdate();

        } catch (SQLException throwables) {

            throwables.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement ps = connection.prepareStatement("Update usr set status = 'Deleted' where id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException throwables) {

            throwables.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
