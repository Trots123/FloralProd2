
package com.flora.controller;

import com.flora.FlorDTO.CartDTO;
import com.flora.services.CartDTOService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cartsDTO")
public class CartDTOController {

    private final CartDTOService cartDTOService;


    public CartDTOController(CartDTOService cartDTOService) {
        this.cartDTOService = cartDTOService;

    }

    @GetMapping("/{clientId}/cart")
    public List<CartDTO> getCartForFront( @PathVariable("clientId") int clientId) {
        return cartDTOService.getForCartFrontByClientId(clientId);

    }








}