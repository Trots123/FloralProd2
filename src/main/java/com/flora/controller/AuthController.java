package com.flora.controller;

import com.flora.FlorDTO.ClientResponseDTO;
import com.flora.FlorDTO.LoginRequest;
import com.flora.FlorDTO.RegisterRequest;

import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;
import com.flora.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;

    // Конструктор
    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            ClientRepository clientRepository) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
    }

    // 1. Получение CSRF-токена
    @GetMapping("/csrf")
    public CsrfToken csrf(
            CsrfToken csrfToken,
            HttpServletResponse response) {

        String token = csrfToken.getToken();

        response.addHeader(
                "Set-Cookie",
                "XSRF-TOKEN=" + token + "; Path=/; SameSite=Lax"
        );

        return csrfToken;
    }

    // 2. Регистрация клиента
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok(
                "Регистрация успешна! Теперь войдите в аккаунт."
        );
    }

    // 3. Вход в аккаунт
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getPhoneNumber(),
                                    request.getPassword()
                            )
                    );

            // Создаём сессию
            httpRequest.getSession();

            // Меняем ID сессии для защиты
            httpRequest.changeSessionId();

            // Создаём SecurityContext
            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

            // Сохраняем авторизацию в HTTP-сессии
            HttpSessionSecurityContextRepository repository =
                    new HttpSessionSecurityContextRepository();

            repository.saveContext(
                    context,
                    httpRequest,
                    httpResponse
            );

            return ResponseEntity.ok(
                    "Вход выполнен успешно!"
            );

        } catch (BadCredentialsException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверный номер телефона или пароль");
        }
    }

    // 4. Получение данных авторизованного клиента
    @GetMapping("/me")
    public ResponseEntity<ClientResponseDTO> me(
            @AuthenticationPrincipal UserDetails user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ClientFlorr client = clientRepository
                .findByPhoneNumber(user.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Клиент не найден"
                        )
                );

        ClientResponseDTO dto = new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getPhoneNumber(),
                client.getBalance()
        );

        return ResponseEntity.ok(dto);
    }
}