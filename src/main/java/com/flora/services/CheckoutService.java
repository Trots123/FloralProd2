package com.flora.services;

import com.flora.FlorDTO.CheckoutRequestDTO;
import com.flora.FlorDTO.CheckoutResponseDTO;
import com.flora.FlorDTO.OrderHistoryDTO;

import com.flora.entity.Cart;
import com.flora.entity.ClientFlorr;
import com.flora.entity.ItemCart;
import com.flora.entity.Order;
import com.flora.entity.OrderItem;
import com.flora.entity.Payment;
import com.flora.entity.ProductFlor;

import com.flora.repository.CartRepository;
import com.flora.repository.ClientRepository;
import com.flora.repository.ItemCartRepository;
import com.flora.repository.OrderRepository;
import com.flora.repository.OrderItemRepository;
import com.flora.repository.PaymentRepository;
import com.flora.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final ItemCartRepository itemCartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    public CheckoutService(
            ClientRepository clientRepository,
            CartRepository cartRepository,
            ItemCartRepository itemCartRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            PaymentRepository paymentRepository
    ) {
        this.clientRepository = clientRepository;
        this.cartRepository = cartRepository;
        this.itemCartRepository = itemCartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
    }

    // =========================================
    // ОФОРМЛЕНИЕ ЗАКАЗА
    // =========================================

    @Transactional
    public CheckoutResponseDTO checkout(String phoneNumber, CheckoutRequestDTO request) {

        if (request == null || request.deliveryAddress() == null || request.deliveryAddress().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Укажите адрес доставки"
            );
        }

        String deliveryAddress =
                request.deliveryAddress().trim();

        ClientFlorr client = clientRepository
                .findByPhoneNumberForUpdate(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Клиент не найден"));

        Cart cart = cartRepository
                .findOpenCartsForUpdate(client.getId(), PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Открытая корзина не найдена"));


        // 4. Получаем товары корзины

        List<ItemCart> cartItems =
                itemCartRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Корзина пустая");
        }

        Map<Integer, Long> quantities = new TreeMap<>();

        for (ItemCart item : cartItems) {
            if (item.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное количество товара");
            }
            int productId = item.getProductFlor().getId();
            quantities.merge(productId, (long) item.getQuantity(), Math::addExact);
        }


        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;


        // 6. Получаем товары с блокировкой
        // и проверяем остатки

        for (Map.Entry<Integer, Long> entry
                : quantities.entrySet()) {

            int productId = entry.getKey();
            long quantity = entry.getValue();

            ProductFlor product = productRepository
                    .findByIdForUpdate(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Товар не найден: " + productId));

            if (product.getPrice() == null
                    || product.getPrice().signum() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная цена товара");
            }

            if (quantity > product.getStockQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Недостаточно товара на складе: " + productId);
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setProductFlor(product);
            orderItem.setProdName(product.getName());
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));

            totalPrice = totalPrice.add(itemTotal);
        }

        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Стоимость заказа должна быть больше нуля");
        }


        if (client.getBalance() == null || client.getBalance().compareTo(totalPrice) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Недостаточно денег на балансе");
        }

        client.setBalance(client.getBalance().subtract(totalPrice));

        for (OrderItem item : orderItems) {
            ProductFlor product = item.getProductFlor();
            product.setStockQuantity(product.getStockQuantity() - Math.toIntExact(item.getQuantity()));
        }


        Order order = new Order();

        order.setCart(cart);
        order.setTotalPrice(totalPrice);
        order.setDeliveryAddress(deliveryAddress);

        Order savedOrder = orderRepository.save(order);


        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
        }

        orderItemRepository.saveAll(orderItems);


        Payment payment = new Payment();

        payment.setOrder(savedOrder);
        payment.setFullCost(totalPrice);
        payment.setPaid(true);

        paymentRepository.save(payment);

        cart.setDralStstus(true);


        return new CheckoutResponseDTO((long) savedOrder.getId(), (long) cart.getId(), totalPrice, "Покупка успешно оформлена");
    }


    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getMyOrders(String phoneNumber) {


        ClientFlorr client = clientRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Клиент не найден"));

        List<Order> orders = orderRepository.findByCartClientIdOrderByIdDesc(client.getId());
        if (orders.isEmpty()) {
            return List.of();
        }

        List<Integer> orderIds = orders.stream().map(Order::getId).toList();

        List<OrderItem> allItems = orderItemRepository.findByOrder_IdIn(orderIds);

        Map<Integer, List<OrderItem>> itemsByOrder = allItems.stream().collect(Collectors.groupingBy(item -> item.getOrder().getId()));


        List<Payment> payments = paymentRepository.findByOrder_IdIn(orderIds);


        Set<Integer> paidOrderIds = payments.stream()
                .filter(Payment::isPaid)
                .map(payment -> payment.getOrder().getId())
                .collect(Collectors.toSet());

        List<OrderHistoryDTO> history = new ArrayList<>();

        for (Order order : orders) {
            List<OrderHistoryDTO.OrderItemDTO> items = itemsByOrder
                            .getOrDefault(order.getId(), List.of())
                            .stream()
                            .sorted(Comparator.comparingLong(OrderItem::getId))
                            .map(item ->
                                    new OrderHistoryDTO.OrderItemDTO(
                                            (long) item.getProductFlor().getId(),
                                            item.getProdName(),
                                            item.getQuantity(),
                                            item.getPrice(),
                                            item.getPrice().multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()
                                                    )
                                            )
                                    )
                            )
                            .toList();
            boolean paid = paidOrderIds.contains(order.getId());

            OrderHistoryDTO dto = new OrderHistoryDTO(
                    (long) order.getId(),
                    (long) order.getCart().getId(),
                    order.getTotalPrice(),
                    paid,
                    order.getCreatedAt(),
                    order.getDeliveryAddress(),
                    items
            );
            history.add(dto);
        }
        return history;
    }
}