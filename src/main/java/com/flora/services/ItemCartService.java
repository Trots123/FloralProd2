package com.flora.services;

import com.flora.entity.ItemCart;
import com.flora.entity.ProductFlor;
import com.flora.repository.ItemCartRepository;
import com.flora.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public ItemCartService(
            ItemCartRepository itemCartRepository,
            CartService cartService,
            ProductRepository productRepository) {

        this.itemCartRepository = itemCartRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    // Получить все позиции корзины
    public List<ItemCart> getAllItemCarts() {
        return itemCartRepository.getAllItemCarts();
    }

    // Получить позицию по ID
    public Optional<ItemCart> getItemCartById(int id) {
        return itemCartRepository.getItemCartById(id);
    }

    // Создать позицию корзины
    public int createItemCart(ItemCart itemCart) {
        if (itemCart.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Количество товара должно быть больше нуля"
            );
        }

        return itemCartRepository.createItemCart(itemCart);
    }

    // Обновить позицию
    public int updateItemCart(ItemCart itemCart) {
        if (itemCart.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Количество товара должно быть больше нуля"
            );
        }

        return itemCartRepository.updateItemCart(itemCart);
    }

    // Удалить позицию
    public int deleteItemCart(int id) {
        return itemCartRepository.deleteItemCart(id);
    }

    // Добавить товар в корзину клиента
    @Transactional
    public void addProductToCart(
            int clientId,
            int prodId,
            int quantity) {

        // 1. Проверяем количество
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Количество должно быть больше нуля"
            );
        }

        // 2. Проверяем существование товара
        ProductFlor product = productRepository.getById(prodId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Товар с ID " + prodId + " не найден"
                ));

        // 3. Получаем или создаём открытую корзину
        int cartId = cartService.getOrCreateCart(clientId);

        System.out.println("clientId = " + clientId);
        System.out.println("cartId = " + cartId);

        // 4. Ищем товар в корзине
        Optional<ItemCart> existingItem =
                itemCartRepository.getItemByCartIdAndProdId(
                        cartId,
                        prodId
                );

        int currentQuantity = existingItem
                .map(ItemCart::getQuantity)
                .orElse(0);

        // 5. Считаем итоговое количество
        int totalQuantity = Math.addExact(
                currentQuantity,
                quantity
        );

        // 6. Проверяем остаток
        if (totalQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Недостаточно товара на складе. Доступно: "
                            + product.getStockQuantity()
            );
        }

        // 7. Обновляем или создаём позицию
        if (existingItem.isPresent()) {

            ItemCart item = existingItem.get();
            item.setQuantity(totalQuantity);

            itemCartRepository.updateItemCart(item);

        } else {

            ItemCart newItem = new ItemCart(
                    0,
                    cartId,
                    prodId,
                    quantity
            );

            System.out.println(
                    "newItem.cartId = " + newItem.getCartId()
            );

            if (newItem.getCartId() != cartId) {
                throw new IllegalStateException(
                        "Ошибка присвоения cartId в ItemCart"
                );
            }

            itemCartRepository.createItemCart(newItem);
        }
    }
    @Transactional
    public void deleteMyItem(int clientId, int itemId) {

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

        // 2. Получаем позицию корзины
        ItemCart item = itemCartRepository
                .getMyItemById(itemId, clientId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Товар не найден в вашей открытой корзине"
                ));

        // 3. Проверяем, что товар существует
        ProductFlor product = productRepository
                .getById(item.getProdId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Продукт не найден"
                ));

        // 4. Проверяем остаток на складе
        int quantityInOtherRows = itemCartRepository
                .getQuantityInCart(
                        item.getCartId(),
                        item.getProdId(),
                        itemId
                );

        long totalQuantity = (long) quantity + quantityInOtherRows;

        if (totalQuantity > product.getStockQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недостаточно цветов на складе. Доступно: "
                            + product.getStockQuantity()
            );
        }

        // 5. Изменяем количество только в своей открытой корзине
        int updated = itemCartRepository.updateMyItemQuantity(
                itemId,
                clientId,
                quantity
        );

        if (updated == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Товар не найден в вашей открытой корзине"
            );
        }
    }
}