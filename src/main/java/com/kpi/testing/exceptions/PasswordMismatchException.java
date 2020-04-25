package com.kpi.testing.exceptions;

public class PasswordMismatchException extends Exception{
    private String message;

    public PasswordMismatchException(String message) {
        super(message);
        this.message = message;
    }

    public PasswordMismatchException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
