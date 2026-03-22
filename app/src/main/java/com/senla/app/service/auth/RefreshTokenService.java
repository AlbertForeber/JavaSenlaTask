package com.senla.app.service.auth;

import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.exceptions.UnavailableAction;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.entity.auth.RefreshToken;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.RefreshTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger logger = LogManager.getLogger(RefreshTokenService.class);
    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-token.expiration:604800000}")
    private Long tokenDuration;

    public RefreshTokenService(@Db RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RefreshToken createToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Instant now = Instant.now();

        RefreshToken refreshToken = new RefreshToken(
                tokenValue,
                user,
                now,
                now.plusMillis(this.tokenDuration)
        );
        return repository.createOrUpdateToken(refreshToken);
    }

    @Transactional
    public RefreshToken rotateToken(String token) {
        RefreshToken oldToken = repository.findByToken(token);
        if (oldToken == null) throw new WrongId("refresh-токен '" + token + "' не найден");

        if (!oldToken.isValid()) {
            if (oldToken.getUsed() && oldToken.getReplacedByToken() != null) {
                handleReuse(oldToken);
            }

            throw new UnavailableAction("обновление refresh-токена", "токен истек или невалиден");
        }

        RefreshToken newToken = createToken(oldToken.getUser());
        repository.createOrUpdateToken(newToken);
        repository.markAsUsed(token, newToken.getToken());

        return newToken;
    }

    // Для логаута
    @Transactional
    public void revokeAllTokensForUser(String username) {
        repository.revokeAllTokensForUser(username);
    }

    @Transactional
    protected void handleReuse(RefreshToken token) {
        logger.warn("ПРЕДУПРЕЖДЕНИЕ БЕЗОПАСНОСТИ: Попытка обновления использованного токена пользователя {}",
                token.getUser().getUsername());

        repository.revokeAllTokensForUser(token.getUser().getUsername());
    }
}
