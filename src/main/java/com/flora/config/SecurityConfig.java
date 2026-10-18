package com.flora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    // 1. Настройка безопасности HTTP-запросов
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Защита от CSRF-атак
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                        .csrfTokenRequestHandler(
                                new CsrfTokenRequestAttributeHandler()
                        )
                )

                // Настройка доступа к API
                .authorizeHttpRequests(auth -> auth

                        // Просмотр товаров доступен всем
                        .requestMatchers(
                                HttpMethod.GET,
                                "/product",
                                "/product/**"
                        ).permitAll()

                        // Регистрация, вход и получение CSRF-токена
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/auth/csrf"
                        ).permitAll()

                        // Остальные запросы требуют авторизации
                        .anyRequest().authenticated()
                )

                // Выход из аккаунта
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")

                        .logoutSuccessHandler(
                                (request, response, authentication) -> {
                                    response.setStatus(200);
                                    response.setContentType(
                                            "text/plain;charset=UTF-8"
                                    );
                                    response.getWriter().write(
                                            "Вы успешно вышли из аккаунта!"
                                    );
                                }
                        )

                        // Удаляем сессию, а не аккаунт
                        .invalidateHttpSession(true)

                        // Очищаем авторизацию
                        .clearAuthentication(true)

                        // Удаляем cookie сессии
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    // 2. Шифрование (хеширование) паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. Менеджер аутентификации
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}