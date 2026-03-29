package com.senla.app.service.auth.user;

import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(@Db UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User createUser(User user) {
        return repository.saveUser(user);
    }
}
