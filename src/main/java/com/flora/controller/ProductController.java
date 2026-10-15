package com.flora.controller;


import com.flora.entity.ProductFlor;
import com.flora.services.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //вывод всех продуктов
    @GetMapping
    public List<ProductFlor> getProduct() {
        return productService.getAllProduct();
    }

    //создание продукта
    @PostMapping
    public void addProduct(@RequestBody ProductFlor product) {
        productService.addProduct(product);
    }

    // поиск по ID
    @GetMapping("/{id}")
    public Optional<ProductFlor> getProductById(
            @PathVariable("id") int id
    ) {
        return productService.getProductById(id);
    }

    // изменение продукта
    @PutMapping("/{id}")
    public boolean updateProduct(
            @PathVariable("id") int id,
            @RequestBody ProductFlor product
    ) {
        return productService.updateProduct(id, product);
    }

    // удаление продукта
    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable("id") int id) {
        return productService.deleteProduct(id);
    }

}
