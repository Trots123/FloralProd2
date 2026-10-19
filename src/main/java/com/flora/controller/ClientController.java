package com.flora.controller;

import com.flora.FlorDTO.TopUpRequestDTO;
import com.flora.entity.ClientFlorr;
import com.flora.services.ClientService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Учебное пополнение собственного баланса
    @PostMapping("/me/top-up")
    public Map<String, BigDecimal> topUp(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody TopUpRequestDTO request
    ) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Необходима авторизация"
            );
        }

        BigDecimal newBalance = clientService.topUpBalance(
                user.getUsername(),
                request.amount()
        );

        return Map.of("balance", newBalance);
    }

    // Административные CRUD-операции пока закрыты.
    // Вернём их после добавления роли ADMIN.

    @GetMapping("/{id}")
    public Optional<ClientFlorr> getClientById(
            @PathVariable("id") int id
    ) {
        throw adminOnly();
    }

    @GetMapping
    public List<ClientFlorr> getAllClients() {
        throw adminOnly();
    }

    @PostMapping
    public void addClient(
            @RequestBody ClientFlorr client
    ) {
        throw adminOnly();
    }

    @PutMapping("/{id}")
    public boolean updateClient(
            @PathVariable("id") int id,
            @RequestBody ClientFlorr client
    ) {
        throw adminOnly();
    }

    @DeleteMapping("/{id}")
    public boolean deleteClient(
            @PathVariable("id") int id
    ) {
        throw adminOnly();
    }

    private ResponseStatusException adminOnly() {
        return new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Операция доступна только администратору"
        );
    }
}