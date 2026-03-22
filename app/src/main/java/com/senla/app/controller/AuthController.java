package com.senla.app.controller;

import com.senla.annotation.LoggingOperation;
import com.senla.app.exceptions.AuthenticationException;
import com.senla.app.model.dto.request.LoginRequest;
import com.senla.app.model.dto.request.RefreshTokenRequest;
import com.senla.app.model.dto.response.AuthenticationResponse;
import com.senla.app.model.entity.auth.RefreshToken;
import com.senla.app.model.entity.auth.User;
import com.senla.app.service.auth.JwtService;
import com.senla.app.service.auth.RefreshTokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @LoggingOperation("аутентификация")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {

        // До аутентификации
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        logger.info("Before authenticate");
        User user;

        // Пробрасываем встроенные ошибки в кастомное исключение
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            user = (User) authentication.getPrincipal(); // После аутентификации
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Неверные учетные данные");
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }

        logger.info("Before jwt");
        String accessToken = jwtService.generateToken(user);

        logger.info("Before refresh");
        String refreshToken = refreshTokenService.createToken(user).getToken();
        AuthenticationResponse response = new AuthenticationResponse(accessToken, refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @LoggingOperation("обновление токена")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
        RefreshToken newToken = refreshTokenService.rotateToken(request.getRefreshToken());
        String newAccessToken = jwtService.generateToken(newToken.getUser());

        AuthenticationResponse response = new AuthenticationResponse(newAccessToken, newToken.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @LoggingOperation("выход")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);

        String username = jwtService.getUsernameFromToken(accessToken);
        refreshTokenService.revokeAllTokensForUser(username);
    }
}