package com.kpi.testing.exceptions;

public class UsernameNotFoundException extends Exception{
    private String message;

    public UsernameNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public UsernameNotFoundException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
