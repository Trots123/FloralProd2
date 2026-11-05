package com.flora.repository;

import com.flora.entity.ProductFlor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductFlor, Integer> {
}