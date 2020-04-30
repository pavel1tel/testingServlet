package com.kpi.testing.exceptions;

public class UnknownReportError extends Exception {
    private String message;

    public UnknownReportError(String message) {
        super(message);
        this.message = message;
    }

    public UnknownReportError() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
