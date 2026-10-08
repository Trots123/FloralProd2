package com.flora.controller;


import com.flora.entity.product_flor;
import com.flora.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FirstController {

    ProductService productService = new ProductService();




   @GetMapping("/testget")
    public List<product_flor> testController (){
        return productService.getAllProduct();
    }

//
    @PostMapping("/testpost")
    public void testPost(@RequestBody product_flor product) {
        productService.addProduct(product);
    }




}
