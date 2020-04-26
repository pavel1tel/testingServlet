package com.kpi.testing.exceptions;


public class UserExistsException extends Exception{
    private String message;

    public UserExistsException(String message) {
        super(message);
        this.message = message;
    }

    public UserExistsException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}

