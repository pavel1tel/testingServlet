package com.kpi.testing.entity;

import java.time.LocalDate;
import java.util.List;

public class Archive extends BaseEntity{

    private User inspectorDecision;
    private Report report;
    private String name;
    private String description;
    private String declineReason;

    public Archive(User inspectorDecision, Report report, String description, String declineReason) {
        this.inspectorDecision = inspectorDecision;
        this.report = report;
        this.description = description;
        this.declineReason = declineReason;
    }

    public Archive(){}

    public static Builder builder() {
        return new Archive().new Builder();
    }

    public class Builder{
        private Builder(){}

        public Builder inspectorDecision(User inspectorDecision){
            Archive.this.setInspectorDecision(inspectorDecision);
            return this;
        }

        public Archive.Builder id(Long id) {
            Archive.this.setId(id);
            return this;
        }

        public Builder description(String description){
            Archive.this.setDescription(description);
            return  this;
        }

        public Builder name(String name){
            Archive.this.setName(name);
            return  this;
        }

        public Builder report(Report report){
            Archive.this.setReport(report);
            return this;
        }

        public Builder declineReason(String declineReason){
            Archive.this.setDeclineReason(declineReason);
            return this;
        }

        public Builder created(LocalDate created) {
            Archive.this.setCreated(created);
            return this;
        }

        public Builder updated(LocalDate updated) {
            Archive.this.setUpdated(updated);
            return this;
        }

        public Archive build() {
            return Archive.this;
        }
    }

    public User getInspectorDecision() {
        return inspectorDecision;
    }

    public void setInspectorDecision(User inspectorDecision) {
        this.inspectorDecision = inspectorDecision;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
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

    @Override
    public String toString() {
        return "Archive{" +
                "id=" + getId() +
                "  inspectorDecision=" + inspectorDecision +
                ", report=" + report +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", declineReason='" + declineReason + '\'' +
                '}';
    }
}
