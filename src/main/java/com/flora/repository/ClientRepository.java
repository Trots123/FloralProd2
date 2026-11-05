package com.flora.repository;

import com.flora.entity.ClientFlorr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientFlorr, Integer> {

    // Найти клиента по телефону
    Optional<ClientFlorr> findByPhoneNumber(String phoneNumber);

    // Атомарно увеличить баланс
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE ClientFlorr c
        SET c.balance = c.balance + :amount
        WHERE c.phoneNumber = :phoneNumber
        """)
    int increaseBalance(
            @Param("phoneNumber") String phoneNumber,
            @Param("amount") BigDecimal amount
    );
}