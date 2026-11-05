package com.flora.services;

import com.flora.entity.Cart;
import com.flora.entity.ItemCart;
import com.flora.entity.ProductFlor;
import com.flora.repository.ItemCartRepository;
import com.flora.repository.ProductRepository;

import jakarta.persistence.EntityManager;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ItemCartService(
            ItemCartRepository itemCartRepository,
            CartService cartService,
            ProductRepository productRepository,
            EntityManager entityManager
    ) {
        this.itemCartRepository = itemCartRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    // Добавить товар в корзину
    @Transactional
    public void addProductToCart(
            int clientId,
            int prodId,
            int quantity
    ) {

        // 1. Проверяем количество
        if (quantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Количество должно быть больше нуля"
            );
        }

        // 2. Получаем товар
        ProductFlor product = productRepository
                .findById(prodId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Товар не найден"
                ));

        // 3. Получаем или создаём корзину
        int cartId = cartService.getOrCreateCart(clientId);

        // 4. Ищем товар в корзине
        Optional<ItemCart> existingItem =
                itemCartRepository
                        .findFirstByCart_IdAndProductFlor_Id(
                                cartId,
                                prodId
                        );

        int currentQuantity = existingItem
                .map(ItemCart::getQuantity)
                .orElse(0);

        // 5. Считаем новое количество
        int totalQuantity;

        try {
            totalQuantity = Math.addExact(
                    currentQuantity,
                    quantity
            );
        } catch (ArithmeticException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Слишком большое количество товара"
            );
        }

        // 6. Проверяем остаток
        if (totalQuantity > product.getStockQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недостаточно товара на складе. Доступно: "
                            + product.getStockQuantity()
            );
        }

        // 7. Обновляем или создаём позицию
        if (existingItem.isPresent()) {

            ItemCart item = existingItem.get();

            item.setQuantity(totalQuantity);

            // Hibernate сохранит изменение при commit

        } else {

            Cart cart = entityManager.getReference(
                    Cart.class,
                    cartId
            );

            ItemCart newItem = new ItemCart();

            newItem.setCart(cart);
            newItem.setProductFlor(product);
            newItem.setQuantity(quantity);

            itemCartRepository.save(newItem);
        }
    }

    // Удалить товар из своей активной корзины
    @Transactional
    public void deleteMyItem(
            int clientId,
            int itemId
    ) {

        int deleted = itemCartRepository.deleteMyItem(
                itemId,
                clientId
        );

        if (deleted == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Товар не найден в вашей открытой корзине"
            );
        }
    }

    // Изменить количество товара
    @Transactional
    public void updateMyItemQuantity(
            int clientId,
            int itemId,
            int quantity
    ) {

        // 1. Проверяем количество
        if (quantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Количество должно быть больше нуля"
            );
        }

        // 2. Находим позицию в своей активной корзине
        ItemCart item = itemCartRepository
                .getMyItemById(itemId, clientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Товар не найден в вашей открытой корзине"
                ));

        // 3. Получаем товар
        ProductFlor product = item.getProductFlor();

        // 4. Проверяем количество в других строках
        Long quantityInOtherRows = itemCartRepository
                .getQuantityInCart(
                        item.getCart().getId(),
                        product.getId(),
                        itemId
                );

        long totalQuantity =
                (long) quantity + quantityInOtherRows;

        // 5. Проверяем остаток
        if (totalQuantity > product.getStockQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недостаточно цветов на складе. Доступно: "
                            + product.getStockQuantity()
            );
        }

        // 6. Обновляем количество
        item.setQuantity(quantity);

        // Hibernate автоматически сохранит изменение
        // в конце транзакции
    }
}