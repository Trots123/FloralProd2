package com.flora.controller;


import com.flora.FlorDTO.AddToCartRequestDTO;
import com.flora.FlorDTO.UpdateCartQuantityDTO;
import com.flora.entity.ClientFlorr;
import com.flora.repository.ClientRepository;
import com.flora.services.ItemCartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final ItemCartService itemCartService;
    private final ClientRepository clientRepository;

    public CartController(
            ItemCartService itemCartService,
            ClientRepository clientRepository
    ) {
        this.itemCartService = itemCartService;
        this.clientRepository = clientRepository;
    }

    // Получить авторизованного клиента
    private ClientFlorr getCurrentClient(UserDetails user) {

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Необходима авторизация"
            );
        }

        return clientRepository
                .findByPhoneNumber(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Клиент не найден"
                ));
    }

    // Добавить товар в свою корзину
    @PostMapping("/my-items")
    public ResponseEntity<String> addProductToMyCart(
            @RequestBody AddToCartRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {

        ClientFlorr client = getCurrentClient(user);

        itemCartService.addProductToCart(
                client.getId(),
                request.getProdId(),
                request.getQuantity()
        );

        return ResponseEntity.ok("Товар добавлен в корзину");
    }

    // Изменить количество товара в своей корзине
    @PutMapping("/my-items/{itemId}")
    public ResponseEntity<String> updateMyCartItem(
            @PathVariable("itemId") int itemId,
            @RequestBody UpdateCartQuantityDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {

        ClientFlorr client = getCurrentClient(user);

        itemCartService.updateMyItemQuantity(
                client.getId(),
                itemId,
                request.quantity()
        );

        return ResponseEntity.ok("Количество товара обновлено");
    }

    // Удалить товар из своей корзины
    @DeleteMapping("/my-items/{itemId}")
    public ResponseEntity<Void> deleteMyCartItem(
            @PathVariable("itemId") int itemId,
            @AuthenticationPrincipal UserDetails user
    ) {

        ClientFlorr client = getCurrentClient(user);

        itemCartService.deleteMyItem(
                client.getId(),
                itemId
        );

        return ResponseEntity.noContent().build();
    }
}