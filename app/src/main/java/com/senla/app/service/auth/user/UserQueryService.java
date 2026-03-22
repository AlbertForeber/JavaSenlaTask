package com.senla.app.service.auth.user;

import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final UserRepository repository;

    public UserQueryService(@Db UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User getUserByUsername(String username) {
        User user = repository.getUserByUsername(username);
        if (user == null) throw new ResourceNotFound("Пользователя с именем '" + username + "' не существует");
        return user;
    }
}
