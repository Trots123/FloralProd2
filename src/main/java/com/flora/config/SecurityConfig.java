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

    // 1. Настройка безопасности приложения
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

                // Настройка доступа к endpoints
                .authorizeHttpRequests(auth -> auth

                        // Главная страница магазина
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error"
                        ).permitAll()

                        // Каталог цветов доступен всем
                        .requestMatchers(
                                HttpMethod.GET,
                                "/product",
                                "/product/**"
                        ).permitAll()

                        // Регистрация, вход и CSRF-токен
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

                        // Адрес выхода
                        .logoutUrl("/auth/logout")

                        // Ответ при успешном выходе
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

                        // Завершаем сессию
                        .invalidateHttpSession(true)

                        // Очищаем авторизацию
                        .clearAuthentication(true)

                        // Удаляем cookie сессии
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    // 2. Хеширование паролей BCrypt
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