package com.senla.app.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.app.controller.AuthController;
import com.senla.app.model.dto.request.LoginRequest;
import com.senla.app.model.dto.request.RefreshTokenRequest;
import com.senla.app.model.entity.auth.RefreshToken;
import com.senla.app.model.entity.auth.User;
import com.senla.app.service.auth.JwtService;
import com.senla.app.service.auth.RefreshTokenService;
import com.senla.app.service.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@DisplayName("Тест эндпоинтов Auth")
@ContextConfiguration(classes = {
        AuthController.class,
        TestSecurityConfig.class,
        AuthEndpointTest.TestConfig.class
})
@WebAppConfiguration
public class AuthEndpointTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtService jwtServiceMock;

    @Autowired
    private RefreshTokenService refreshTokenServiceMock;

    @Autowired
    private AuthenticationManager authenticationManagerMock;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Mockito.reset(jwtServiceMock, refreshTokenServiceMock, authenticationManagerMock);
    }

    @Configuration
    static class TestConfig {

        @Bean
        @Primary
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        @Primary
        public RefreshTokenService refreshTokenService() {
            return Mockito.mock(RefreshTokenService.class);
        }

        @Bean
        @Primary
        public AuthenticationManager authenticationManager() {
            return Mockito.mock(AuthenticationManager.class);
        }

        @Bean
        @Primary
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    @Tag("integrate")
    @DisplayName("/api/auth/login должен отказать в аутентификации при неверный данных входа")
    @WithAnonymousUser
    public void loginShouldDenyWhenInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        when(authenticationManagerMock.authenticate(any())).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isUnauthorized());

        verifyNoInteractions(jwtServiceMock, refreshTokenServiceMock);
    }

    @Test
    @Tag("integrate")
    @DisplayName("/api/auth/login должен вернуть access и refresh токен, если данные входа верны")
    @WithAnonymousUser
    public void loginShouldReturnWhenValidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        User user = new User(1,
                "",
                "",
                Collections.emptyList()
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities()
        );

        when(authenticationManagerMock.authenticate(any())).thenReturn(authentication);
        when(jwtServiceMock.generateToken(any(User.class))).thenReturn("access_token");
        when(refreshTokenServiceMock.createToken(any(User.class))).thenReturn(
                new RefreshToken("refresh_token", user, null, null)
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
                .andExpect(status().isOk())
                .andExpect(
                        content().json("{'accessToken': \"access_token\", 'refreshToken': \"refresh_token\"}")
                );
    }

    @Test
    @Tag("integrate")
    @DisplayName("/api/auth/refresh должен вернуть новые access и refresh токен")
    @WithAnonymousUser
    public void refreshShouldReturnNewTokens() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        when(refreshTokenServiceMock.rotateToken(any())).thenReturn(
                new RefreshToken("refresh_token", null, null, null)
        );
        when(jwtServiceMock.generateToken(any())).thenReturn("access_token");

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest))
        )
                .andExpect(status().isOk())
                .andExpect(
                        content().json("{'accessToken': \"access_token\", 'refreshToken': \"refresh_token\"}")
                );
    }

    @Test
    @Tag("integrate")
    @DisplayName("/api/auth/logout должен произвести выход, если в заголовке передан токен")
    @WithMockUser
    public void logoutShouldRevokeAllTokensWhenTokenInHeader() throws Exception {
        when(jwtServiceMock.getUsernameFromToken(anyString())).thenReturn("access_token");
        doNothing().when(refreshTokenServiceMock).revokeAllTokensForUser(anyString());

        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer access_token"))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("integrate")
    @DisplayName("/api/auth/logout должен произвести выход, если в заголовке нет токена")
    @WithMockUser
    public void logoutShouldRevokeAllTokensWhenNoToken() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(jwtServiceMock, refreshTokenServiceMock);
    }
}
