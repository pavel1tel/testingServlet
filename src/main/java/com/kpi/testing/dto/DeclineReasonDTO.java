package com.kpi.testing.dto;

import javax.validation.constraints.NotNull;

public class DeclineReasonDTO {
    @NotNull
    private String declineReason;

    public DeclineReasonDTO(String declineReason) {
        this.declineReason = declineReason;
    }

    public DeclineReasonDTO() {
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }
}

