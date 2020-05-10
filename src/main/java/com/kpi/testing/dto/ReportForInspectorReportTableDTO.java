package com.kpi.testing.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class ReportForInspectorReportTableDTO {
    @NotNull
    private Long id;
    @NotNull
    private String Name;
    @NotNull
    private LocalDate created;
    @NotNull
    private LocalDate updated;
    @NotNull
    private String description;

    public ReportForInspectorReportTableDTO(Long id, String name, LocalDate created, LocalDate updated, String description) {
        this.id = id;
        Name = name;
        this.created = created;
        this.updated = updated;
        this.description = description;
    }

    public ReportForInspectorReportTableDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportForInspectorReportTableDTO that = (ReportForInspectorReportTableDTO) o;
        return id.equals(that.id) &&
                Name.equals(that.Name) &&
                created.equals(that.created) &&
                updated.equals(that.updated) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Name, created, updated, description);
    }
}
