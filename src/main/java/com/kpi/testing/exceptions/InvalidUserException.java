package com.kpi.testing.exceptions;

public class InvalidUserException extends Exception{
    private String message;

    public InvalidUserException(String message) {
        super(message);
        this.message = message;
    }

    public InvalidUserException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
