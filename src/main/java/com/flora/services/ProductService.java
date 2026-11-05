package com.flora.services;

import com.flora.entity.ProductFlor;
import com.flora.repository.ProductRepository;
import jakarta.transaction.Transactional;
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
        return productRepository.findAll();
    }

    //добавление, создание продукта
    public void addProduct(ProductFlor product) {
        productRepository.saveAndFlush(product);
    }

    // поиск продукта по ID
    public ProductFlor getProductById(int id) {
        return productRepository.getReferenceById(id);
    }


    // Обновление продукта
    public boolean updateProduct(int id, ProductFlor product) {
        return productRepository.updateProduct(
                id,
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStockQuantity()
        ) > 0;
    }

    // Удаление продукта
    public boolean deleteProduct(int id) {
        return productRepository.deleteProduct(id) > 0;
    }
}
