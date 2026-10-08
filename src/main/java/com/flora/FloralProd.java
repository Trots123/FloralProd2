package com.flora;

import com.flora.entity.product_flor;
import com.flora.repository.TestRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication

public class FloralProd {

    public static void main(String[] args) {
        SpringApplication.run(FloralProd.class, args);

     /*   TestRepository repository = new TestRepository();

        repository.getInsert();

        List<product_flor> products = repository.getAll();

        for (product_flor product : products) {
            System.out.println(
                    product.getId() + " " +
                            product.getName() + " " +
                            product.getPrice() + " " +
                            product.getDescription()
            );

        }


      */
    }
}
