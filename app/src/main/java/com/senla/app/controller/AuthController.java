package com.senla.app.controller;

import com.senla.app.model.dto.request.LoginRequest;
import com.senla.app.model.dto.request.RefreshTokenRequest;
import com.senla.app.model.dto.response.AuthenticationResponse;
import com.senla.app.model.entity.auth.RefreshToken;
import com.senla.app.model.entity.auth.User;
import com.senla.app.service.auth.JwtService;
import com.senla.app.service.auth.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthController(JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {

        // До аутентификации
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(token);

        // После аутентификации
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createToken(user).getToken();
        AuthenticationResponse response = new AuthenticationResponse(accessToken, refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
        RefreshToken newToken = refreshTokenService.rotateToken(request.getRequestToken());
        String newAccessToken = jwtService.generateToken(newToken.getUser());

        AuthenticationResponse response = new AuthenticationResponse(newToken.getToken(), newAccessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);

        String username = jwtService.getUsernameFromToken(accessToken);
        refreshTokenService.revokeAllTokensForUser(username);
    }
}