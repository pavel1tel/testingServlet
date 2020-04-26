package com.kpi.testing.validators;

import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.InvalidUserException;

import java.util.Locale;

public class NewUserValidator {

    public void validate(User user) throws InvalidUserException {
        if(user.getPassword().length() < 8) {
            throw new InvalidUserException();
        }
        try {
            double d = Double.parseDouble(user.getPassword());
            throw new InvalidUserException();
        } catch (NumberFormatException ignored) {
            
        }

        if(!user.getConfirmPassword().equals(user.getPassword())){
            throw new InvalidUserException();
        }
    }
}
