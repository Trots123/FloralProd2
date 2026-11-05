package com.flora.repository;

import com.flora.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem , Integer> {

    // Получить товары заказа по ID, отсортированные по ID
    List<OrderItem> getItemsByOrderId (int orderId);
}