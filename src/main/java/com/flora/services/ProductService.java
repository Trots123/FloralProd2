package com.flora.services;

import com.flora.entity.product_flor;
import com.flora.repository.TestRepository;

import java.util.List;

public class ProductService {
   private TestRepository testRepository = new TestRepository();



    public List<product_flor> getAllProduct() {
        return testRepository.getAll();
    }

    public List<product_flor> addProduct(product_flor product) {
        return testRepository.getInsert(product);
    }

}
