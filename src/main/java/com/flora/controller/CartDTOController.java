
package com.flora.controller;

import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;
import com.flora.services.CartDTOService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cartsDTO")
public class CartDTOController {

    private final CartDTOService cartDTOService;
    private final ClientRepository clientRepository;

    // Конструктор
    public CartDTOController(
            CartDTOService cartDTOService,
            ClientRepository clientRepository) {

        this.cartDTOService = cartDTOService;
        this.clientRepository = clientRepository;
    }

    // Получить корзину авторизованного клиента
    @GetMapping("/my-cart")
    public ResponseEntity<?> getMyCart(
            @AuthenticationPrincipal UserDetails user) {

        // Проверяем, что пользователь авторизован
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        // Находим клиента по номеру телефона
        ClientFlorr client = clientRepository
                .findByPhoneNumber(user.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Клиент не найден"
                        )
                );

        // Получаем корзину этого клиента
        return ResponseEntity.ok(
                cartDTOService.getForCartFrontByClientId(client.getId())
        );
    }
}