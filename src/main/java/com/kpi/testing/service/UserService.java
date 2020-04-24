package com.kpi.testing.service;

import com.kpi.testing.dao.DaoFactory;
import com.kpi.testing.dao.UserDAO;
import com.kpi.testing.entity.User;
import com.kpi.testing.exceptions.UsernameNotFoundException;

public class UserService {
    UserDAO userDao;

    public UserService() {
        DaoFactory factory = DaoFactory.getInstance();
        userDao = factory.createUserDao();
    }

    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", s)));
    }
}
