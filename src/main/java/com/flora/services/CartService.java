package com.flora.services;

import com.flora.entity.Cart;
import com.flora.entity.ClientFlorr;
import com.flora.repository.CartRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final EntityManager entityManager;

    public CartService(
            CartRepository cartRepository,
            EntityManager entityManager
    ) {
        this.cartRepository = cartRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public int getOrCreateCart(int clientId) {

        Optional<Cart> cart =
                cartRepository.getCartByClientId(clientId);

        if (cart.isPresent()) {
            return cart.get().getId();
        }

        ClientFlorr client =
                entityManager.getReference(
                        ClientFlorr.class, clientId
                );

        Cart newCart = new Cart();
        newCart.setClient(client);
        newCart.setDralStstus(false);

        Cart savedCart = cartRepository.save(newCart);

        return savedCart.getId();
    }
}












/*
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
}

 */