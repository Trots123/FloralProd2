package com.flora.services;

import com.flora.entity.ProductFlor;
import com.flora.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //поиск всех продуктов
    public List<ProductFlor> getAllProduct() {
        return productRepository.getAll();
    }

    //добавление, создание продукта
    public void addProduct(ProductFlor product) {
        productRepository.getInsert(product);
    }

    // поиск продукта по ID
    public Optional<ProductFlor> getProductById(int id) {
        return productRepository.getById(id);
    }

    //изменение продукта
    public boolean updateProduct(int id, ProductFlor product) {
        return productRepository.updateProduct(id, product) > 0;
    }

    // удаление продукта
    public boolean deleteProduct(int id) {
        return productRepository.deleteProduct(id) > 0;
    }
}
