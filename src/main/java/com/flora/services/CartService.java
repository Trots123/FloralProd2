package com.flora.services;
import com.flora.entity.Cart;
import com.flora.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    //  получить все корзины
    public List<Cart> getAllCarts() {
        return cartRepository.getAllCarts();
    }

    //  получить корзину по ID
    public Optional<Cart> getCartById(int id) {
        return cartRepository.getCartById(id);
    }

    // создать корзину для клиента
    public int createCart(int clientId) {
        return cartRepository.createCart(clientId);
    }

    // обновить корзину
    public boolean updateCart(Cart cart) {
        return cartRepository.updateCart(cart) > 0;
    }

    // удалить корзину
    public boolean deleteCart(int id) {
        return cartRepository.deleteCart(id) > 0;
    }

    // Получить существующую корзину или создать новую
    public Cart getOrCreateCart(int clientId) {
        return cartRepository.getCartByClientId(clientId)
                .orElseGet(() -> {
                    int cartId = cartRepository.createCart(clientId);
                    return new Cart(cartId, clientId, true);
                });
    }
}