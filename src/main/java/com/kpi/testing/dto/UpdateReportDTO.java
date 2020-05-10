package com.kpi.testing.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UpdateReportDTO {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String declineReason;

    public UpdateReportDTO(Long id, String name, String description, String declineReason) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.declineReason = declineReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UpdateReportDTO() {
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

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReportDTO that = (UpdateReportDTO) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                Objects.equals(declineReason, that.declineReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, declineReason);
    }
}
