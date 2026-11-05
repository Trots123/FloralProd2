package com.flora.repository;

import com.flora.entity.ProductFlor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductFlor, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT p FROM ProductFlor p
    WHERE p.id = :id
    """)
    Optional<ProductFlor> findByIdForUpdate(
            @Param("id") int id
    );

    @Modifying
    @Query("""
            UPDATE ProductFlor p
            SET p.name = :name,
                p.price = :price,
                p.description = :description,
                p.stockQuantity = :stockQuantity
            WHERE p.id = :id
            """)
    int updateProduct(
            @Param("id") int id,
            @Param("name") String name,
            @Param("price") BigDecimal price,
            @Param("description") String description,
            @Param("stockQuantity") int stockQuantity
    );


    // Удаление продукта
    @Modifying
    @Query("""
        DELETE FROM ProductFlor p
        WHERE p.id = :id
        """)
    int deleteProduct(@Param("id") int id);
}
