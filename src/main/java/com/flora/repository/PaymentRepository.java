package com.flora.repository;


import com.flora.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment , Integer> {


    @Modifying
    @Query("""
            UPDATE Payment p
            SET p.fullCost = :fullCost,
                p.paid = :paid,
                p.orderId = :orderId
            WHERE p.orderId = :orderId
            """)
    int updatePaymentByOrderId(
            @Param("full_cost") BigDecimal fullCost,
            @Param("paid") boolean paid,
            @Param("order_id") int orderId
    );


    @Modifying
    @Query("""
        DELETE FROM Payment p
        WHERE p.orderId = :orderId
        """)
    int deletePayment(@Param("orderId") int orderId);

    List<Payment> findByOrder_IdIn(
            List<Integer> orderIds
    );
}
