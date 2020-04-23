package com.kpi.testing.dao;

import com.kpi.testing.entity.User;
import com.kpi.testing.entity.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends BaseDAO<User>{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);
}
