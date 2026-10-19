package com.flora.controller;

import com.flora.FlorDTO.CheckoutResponseDTO;
import com.flora.FlorDTO.OrderHistoryDTO;
import com.flora.services.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public CheckoutResponseDTO checkout(
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Необходима авторизация"
            );
        }

        return checkoutService.checkout(user.getUsername());
    }

    @GetMapping("/my-orders")
    public List<OrderHistoryDTO> getMyOrders(
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Необходима авторизация"
            );
        }

        return checkoutService.getMyOrders(user.getUsername());
    }
}