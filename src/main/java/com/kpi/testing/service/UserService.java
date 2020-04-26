package com.kpi.testing.service;

import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;
import com.kpi.testing.entity.enums.Status;
import com.kpi.testing.exceptions.InvalidUserException;
import com.kpi.testing.exceptions.UserExistsException;
import com.kpi.testing.exceptions.UsernameNotFoundException;
import com.kpi.testing.util.BCryptEncoder;
import com.kpi.testing.validators.NewUserValidator;

import javax.servlet.http.HttpServletRequest;

public class UserService {
    UserDAO userDao;

    public UserService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDao = factory.createUserDao();
    }

    public User loadUserByEmail(String s) throws UsernameNotFoundException {
        return userDao.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", s)));
    }

    public void registration (User user) throws InvalidUserException, UserExistsException {
        NewUserValidator validator = new NewUserValidator();
        BCryptEncoder encoder = new BCryptEncoder();
        validator.validate(user);
        user.setStatus(Status.Active);
        user.setRole(Role.ROLE_USER);
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            userDao.create(user);
        } catch (RuntimeException ex){
            throw new UserExistsException();
        }
    }

    public User extractUserFromRegistration(HttpServletRequest requset) {
        return  User.builder()
                .username(requset.getParameter("username"))
                .email(requset.getParameter("email"))
                .password(requset.getParameter("password"))
                .confirmPassword(requset.getParameter("confirmPassword"))
                .build();
    }
}
