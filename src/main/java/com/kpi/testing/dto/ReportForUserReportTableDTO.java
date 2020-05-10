package com.kpi.testing.dto;


import com.kpi.testing.entity.enums.ReportStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class ReportForUserReportTableDTO {
    @NotNull
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private ReportStatus status;
    @NotNull
    private LocalDate created;
    @NotNull
    private LocalDate updated;
    @NotNull
    private String declineReason;

    public ReportForUserReportTableDTO(String id, String name, String description, ReportStatus status, LocalDate created, LocalDate updated, String reason) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.declineReason = reason;
    }

    public ReportForUserReportTableDTO() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String reason) {
        this.declineReason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportForUserReportTableDTO that = (ReportForUserReportTableDTO) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                status == that.status &&
                created.equals(that.created) &&
                updated.equals(that.updated) &&
                Objects.equals(declineReason, that.declineReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, created, updated, declineReason);
    }
}
