package com.flora.services;

import com.flora.entity.ProductFlor;
import com.flora.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductFlor> getAllProduct() {
        return productRepository.getAll();
    }

    public void addProduct(ProductFlor product) {
        productRepository.getInsert(product);
    }
}
