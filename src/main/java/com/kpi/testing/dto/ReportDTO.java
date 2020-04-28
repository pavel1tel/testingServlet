package com.kpi.testing.dto;


import javax.validation.constraints.NotNull;

public class ReportDTO {
    @NotNull
    private String name;
    @NotNull
    private String description;

    public ReportDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ReportDTO() {
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
}
