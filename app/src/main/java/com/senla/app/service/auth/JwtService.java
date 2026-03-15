package com.senla.app.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // Для чего нам столько функций?
    // Как выглядела бы альтернативная настройка в случае ассиметричной шифровки?

    // TODO добавить в конфигурацию
    // Ключ для подписи и проверки
    @Value("${jwt.secret}")
    private String secretKey;

    // TODO добавить в конфу
    // Время жизни токена, по умолчанию сутки
    // Поставил полчаса по умолчанию
    @Value("${jwt.expiration:1800000}")
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Место для добавления dop claims
        // TODO role + permissions
        claims.put("...", userDetails.getAuthorities());

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .claims(claims) // Кастомные claims
                .subject(subject) // Username как subject
                .issuedAt(now) // Время выдачи
                .expiration(expiryDate) // Время истечения
                .signWith(getSigningKey()) // Подписываем секретным ключом
                .compact(); // Собираем токен в строку
    }

    // Извлекает username из токена
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Извлекает дату истечения из токена
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    // Извлекает любой claim, через resolver фукнцию
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        claimsResolver.apply(claims);
    }

    // Извлекает все claims из токена, с автопроверкой
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload(); // payload с claims
    }

    // Проверяет не истек ли токен
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Валидирует токен, проверяет username и истечение
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Создает секретный ключ для подписи токенов HMAC-SHA256
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes); // Фабричный метод
    }
}
