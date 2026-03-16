package com.senla.app.controller;

import com.senla.annotation.repo_qualifiers.Db;
import com.senla.app.model.entity.auth.User;
import com.senla.app.repository.UserRepository;
import com.senla.app.service.auth.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(JwtService jwtService, @Db UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("login")
    @Transactional
    public ResponseEntity<String> post(@RequestBody String username) {
        User user = userRepository.getUserByUsername(username);
        // TODO пароль + refresh
        return ResponseEntity.ok(jwtService.generateToken(user));
    }
}
