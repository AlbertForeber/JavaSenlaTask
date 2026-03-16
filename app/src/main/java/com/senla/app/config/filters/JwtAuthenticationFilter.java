package com.senla.app.config.filters;

import com.senla.app.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authentication");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            // Если токена нет, переходим к следующему фильтру
            // Запрос проходит без авторизации
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7); // Убираем Bearer и пробел
        String username;

        try {
            username = jwtService.getUsernameFromToken(jwt);
        } catch (Exception e) {
            // При проблемах с токеном (проверка подписи) тоже отправляем без авторизации
            logger.error("JWT Token validation failed {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Проверяем, что еще не авторизованы
        if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (userDetails != null && jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Устанавливаем аутентификаци.
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(request, response);
    }
}
