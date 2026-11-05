package com.flora.repository;

import com.flora.entity.ClientFlorr;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientFlorr, Integer> {



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT c FROM ClientFlorr c
    WHERE c.phoneNumber = :phoneNumber
    """)
    Optional<ClientFlorr> findByPhoneNumberForUpdate(
            @Param("phoneNumber") String phoneNumber
    );

    Optional<ClientFlorr> findByPhoneNumber(String phoneNumber);

        // Пополнение баланса
        @Modifying(flushAutomatically = true, clearAutomatically = true)
        @Query("""
        UPDATE ClientFlorr c
        SET c.balance = c.balance + :amount
        WHERE c.phoneNumber = :phoneNumber
        """)
        int increaseBalance(
                @Param("phoneNumber") String phoneNumber,
                @Param("amount") BigDecimal amount
        );

        // Получение нового баланса
        @Query("""
        SELECT c.balance
        FROM ClientFlorr c
        WHERE c.phoneNumber = :phoneNumber
        """)
        Optional<BigDecimal> findBalanceByPhoneNumber(
                @Param("phoneNumber") String phoneNumber
        );
    }
