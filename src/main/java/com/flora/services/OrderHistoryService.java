package com.flora.services;

import com.flora.FlorDTO.OrderHistoryDTO;
import com.flora.FlorDTO.OrderSummaryDTO;
import com.flora.FlorDTO.OrderHistoryItemRowDTO;


import com.flora.repository.DTO.OrderHistoryDTORepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderHistoryDTORepository repository;

    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getOrderHistory(int clientId) {

        // 1. Получаем все заказы
        List<OrderSummaryDTO> orders =
                repository.findOrdersByClientId(clientId);

        if (orders.isEmpty()) {
            return List.of();
        }

        // 2. Получаем ID заказов
        List<Integer> orderIds = orders.stream()
                .map(OrderSummaryDTO::orderId)
                .toList();

        // 3. Получаем все товары одним запросом
        List<OrderHistoryItemRowDTO> rows = repository.findItemsByOrderIds(orderIds);

        // 4. Группируем товары по заказам
        Map<Integer, List<OrderHistoryDTO.OrderItemDTO>> itemsByOrder = rows.stream()
                        .collect(Collectors.groupingBy(OrderHistoryItemRowDTO::orderId,
                                Collectors.mapping(row -> new OrderHistoryDTO.OrderItemDTO(
                                                row.prodId(),
                                                row.prodName(),
                                                row.quantity(),
                                                row.price(),
                                                row.price().multiply(java.math.BigDecimal.valueOf(row.quantity()))),
                                        Collectors.toList())));

        // 5. Собираем историю заказов
        return orders.stream()
                .map(order -> new OrderHistoryDTO(
                        order.orderId(),
                        order.cartId(),
                        order.totalPrice(),
                        order.paid(),
                        order.createdAt(),
                        order.deliveryAddress(),
                        itemsByOrder.getOrDefault(
                                order.orderId(),
                                List.of()
                        )
                ))
                .toList();
    }
}