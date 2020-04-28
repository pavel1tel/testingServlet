package com.kpi.testing.dto;

import javax.validation.constraints.NotNull;

public class DeclineReasonDTO {
    @NotNull
    private String reason;

    public DeclineReasonDTO(String reason) {
        this.reason = reason;
    }

    public DeclineReasonDTO() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

