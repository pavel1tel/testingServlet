package com.kpi.testing.entity;

import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.entity.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private List<Report> reportsInspected = new ArrayList<>();;
    private List<Report> reportsOwned = new ArrayList<>();;
    private Status status;
    private Role role;
    private String confirmPassword;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", reportsInspected=" + reportsInspected +
                ", reportsOwned=" + reportsOwned +
                ", status=" + status +
                ", role=" + role +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }

    public User(String username, String password, String email, List<Report> reportsInspected, List<Report> reportsOwned, Status status, Role role, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.reportsOwned = reportsOwned;
        this.reportsInspected = reportsInspected;
        this.status = status;
        this.role = role;
        this.confirmPassword = confirmPassword;
    }

    public static Builder builder() {
        return new User().new Builder();
    }

    public User() { }

    public class Builder {
        private Builder(){ }

        public Builder id(Long id) {
            User.this.setId(id);
            return this;
        }

        public Builder username(String username) {
            User.this.setUsername(username);
            return this;
        }

        public Builder email(String email) {
            User.this.setEmail(email);
            return this;
        }

        public Builder password(String password) {
            User.this.setPassword(password);
            return this;
        }

        public Builder confirmPassword(String confirmPassword) {
            User.this.setConfirmPassword(confirmPassword);
            return this;
        }

        public Builder role(Role role) {
            User.this.setRole(role);
            return this;
        }

        public Builder status(Status status) {
            User.this.setStatus(status);
            return this;
        }

        public Builder created(LocalDate created) {
            User.this.setCreated(created);
            return this;
        }

        public Builder updated(LocalDate updated) {
            User.this.setUpdated(updated);
            return this;
        }

        public User build() {
            return User.this;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Report> getReportsInspected() {
        return reportsInspected;
    }

    public void setReportsInspected(List<Report> reportsInspected) {
        this.reportsInspected = reportsInspected;
    }

    public List<Report> getReportsOwned() {
        return reportsOwned;
    }

    public void setReportsOwned(List<Report> reportsOwned) {
        this.reportsOwned = reportsOwned;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
