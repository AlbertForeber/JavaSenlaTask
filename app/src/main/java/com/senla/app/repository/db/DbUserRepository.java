package com.senla.app.repository.db;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
@Db
public class DbUserRepository implements UserRepository {

    private static final Logger logger = LogManager.getLogger(DbUserRepository.class);

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
        logger.info(username);
        User result = userDao.findById(username, true);
        logger.info(result);
        return result;
    }
}
