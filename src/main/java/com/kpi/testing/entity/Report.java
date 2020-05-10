package com.kpi.testing.entity;

import com.kpi.testing.entity.enums.ReportStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Report extends BaseEntity{
    private String name;
    private String description;
    private ReportStatus status;
    private User owner;
    private List<User> inspectors = new ArrayList<>();
    private String declineReason;
    private List<Archive> archives = new ArrayList<>();

    public Report(String name, String description, ReportStatus status, User owner, List<User> inspectors, String declineReason, List<Archive> archives) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.owner = owner;
        this.inspectors = inspectors;
        this.archives = archives;
        this.declineReason = declineReason;
    }

    public Report() {}

    public static Builder builder() {
        return new Report().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder id(Long id) {
            Report.this.setId(id);
            return this;
        }

        public Builder name(String name){
            Report.this.setName(name);
            return this;
        }

        public Builder description(String description){
            Report.this.setDescription(description);
            return this;
        }

        public Builder declineReason(String declineReason){
            Report.this.setDeclineReason(declineReason);
            return this;
        }

        public Builder status(ReportStatus status){
            Report.this.setStatus(status);
            return this;
        }

        public Builder owner(User owner){
            Report.this.setOwner(owner);
            return this;
        }

        public Builder inspectors(List<User> inspectors){
            Report.this.setInspectors(inspectors);
            return this;
        }

        public Builder archives(List<Archive> archives){
            Report.this.setArchives(archives);
            return this;
        }

        public Report.Builder created(LocalDate created) {
            Report.this.setCreated(created);
            return this;
        }

        public Report.Builder updated(LocalDate updated) {
            Report.this.setUpdated(updated);
            return this;
        }

        public Report build(){
            return Report.this;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getInspectors() {
        return inspectors;
    }

    public void setInspectors(List<User> inspectors) {
        this.inspectors = inspectors;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public List<Archive> getArchives() {
        return archives;
    }

    public void setArchives(List<Archive> archives) {
        this.archives = archives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return name.equals(report.name) &&
                description.equals(report.description) &&
                status == report.status &&
                owner.equals(report.owner) &&
                inspectors.equals(report.inspectors) &&
                Objects.equals(declineReason, report.declineReason) &&
                Objects.equals(archives, report.archives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, owner, inspectors, declineReason, archives);
    }
}