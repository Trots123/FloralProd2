package com.flora.controller;
import com.flora.entity.ItemCart;
import com.flora.services.ItemCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item_carts")
public class ItemCartController {

    private final ItemCartService itemCartService;

    public ItemCartController(ItemCartService itemCartService) {
        this.itemCartService = itemCartService;
    }

    // Получить все позиции корзин
    @GetMapping
    public List<ItemCart> getAllItemCarts() {
        return itemCartService.getAllItemCarts();
    }

    // Получить позицию по ID
    @GetMapping("/{id}")
    public Optional<ItemCart> getItemCartById(@PathVariable int id) {
        return itemCartService.getItemCartById(id);
    }
//    // CREATE: создать корзину
//    @PostMapping
//    public int createCart(@RequestParam("clientId") int clientId) {
//        return cartService.createCart(clientId);
//    }


    // Добавить позицию в корзину
    @PostMapping
    public int createItemCart(@RequestBody ItemCart itemCart) {
        return itemCartService.createItemCart(itemCart);
    }

    // Изменить позицию корзины
    @PutMapping("/{id}")
    public int updateItemCart(
            @PathVariable int id,
            @RequestBody ItemCart itemCart) {

        itemCart.setId(id);
        return itemCartService.updateItemCart(itemCart);
    }

    // Удалить позицию по ID
    @DeleteMapping("/{id}")
    public int deleteItemCart(@PathVariable int id) {
        return itemCartService.deleteItemCart(id);
    }
}