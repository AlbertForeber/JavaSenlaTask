package com.senla.app.repository.db;

import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.db.dao.GenericDao;
import com.senla.app.model.entity.auth.RefreshToken;
import com.senla.app.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Db
public class DbRefreshTokenRepository implements RefreshTokenRepository {

    private final GenericDao<RefreshToken, Integer, Object> refreshTokenDao;

    public DbRefreshTokenRepository(GenericDao<RefreshToken, Integer, Object> refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    @Override
    public RefreshToken findByToken(String token) {
        List<RefreshToken> result = refreshTokenDao.findByField("token", token, true, false);
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public List<RefreshToken> findByUsername(String username) {
        return refreshTokenDao.findByField("username", username, true, true);
    }

    @Override
    public void deleteByUsername(String username) {
        findByUsername(username).forEach(o -> refreshTokenDao.delete(o.getId()));
    }

    @Override
    public RefreshToken createOrUpdateToken(RefreshToken token) {
        return refreshTokenDao.save(token);
    }

    @Override
    public void markAsUsed(String oldToken, String newToken) {
        RefreshToken toMark = findByToken(oldToken);

        if (toMark != null) {
            toMark.setUsed(true);
            toMark.setReplacedByToken(newToken);

            refreshTokenDao.update(toMark);
        }
    }

    // На случай подозрения на утечку
    @Override
    public void revokeAllTokensForUser(String username) {
        findByUsername(username).forEach(o -> {
            o.setRevoked(true);
            refreshTokenDao.update(o);
        });
    }
}
