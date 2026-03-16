package com.senla.app.repository;

import com.senla.app.model.entity.auth.User;

public interface UserRepository {
    User saveUser(User user);
    User getUserByUsername(String username);
}
