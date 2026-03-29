package com.senla.app.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@Db
public class DbUserRepository implements UserRepository {

    private final GenericDao<User, String, Object> userDao;

    public DbUserRepository(@Hibernate GenericDao<User, String, Object> userDao) {
        this.userDao = userDao;
    }

    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findById(username, true);
    }
}
