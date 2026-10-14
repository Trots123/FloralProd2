package com.flora.services;

import com.flora.entity.ItemCart;
import com.flora.repository.ItemCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;

    public ItemCartService(ItemCartRepository itemCartRepository) {
        this.itemCartRepository = itemCartRepository;
    }

    // Получить все позиции корзины
    public List<ItemCart> getAllItemCarts() {
        return itemCartRepository.getAllItemCarts();
    }

    // Получить позицию по ID
    public Optional<ItemCart> getItemCartById(int id) {
        return itemCartRepository.getItemCartById(id);
    }

    // Добавить позицию в корзину
    public int createItemCart(ItemCart itemCart) {
        if (itemCart.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Количество товара должно быть больше нуля"
            );
        }

        return itemCartRepository.createItemCart(itemCart);
    }

    // Изменить позицию корзины
    public int updateItemCart(ItemCart itemCart) {
        if (itemCart.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Количество товара должно быть больше нуля"
            );
        }

        return itemCartRepository.updateItemCart(itemCart);
    }

    // Удалить позицию по ID
    public int deleteItemCart(int id) {
        return itemCartRepository.deleteItemCart(id);
    }
}
