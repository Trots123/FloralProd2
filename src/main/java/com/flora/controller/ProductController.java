package com.flora.controller;


import com.flora.entity.ProductFlor;
import com.flora.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/testget")
    public List<ProductFlor> testController() {
        return productService.getAllProduct();
    }


    @PostMapping("/testpost")
    public void testPost(@RequestBody ProductFlor product) {
        productService.addProduct(product);
    }


}
