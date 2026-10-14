package com.flora.controller;

import com.flora.entity.Cart;
import com.flora.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    
    // READ: получить все корзины
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }
    

    // READ: получить корзину по ID
    @GetMapping("/{id}")
    public Optional<Cart> getCartById(@PathVariable(name = "product_id") int id) {
        return cartService.getCartById(id);
    }

    // CREATE: создать корзину
    @PostMapping
    public int createCart(@RequestParam int clientId) {
        return cartService.createCart(clientId);
    }

    // UPDATE: обновить корзину
    @PutMapping("/{id}")
    public boolean updateCart(
            @PathVariable int id,
            @RequestBody Cart cart) {

        cart.setId(id);
        return cartService.updateCart(cart);
    }

    // DELETE: удалить корзину
    @DeleteMapping("/{id}")
    public boolean deleteCart(@PathVariable int id) {
        return cartService.deleteCart(id);
    }
}