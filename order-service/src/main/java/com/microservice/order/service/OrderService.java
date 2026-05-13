package com.microservice.order.service;

import com.microservice.order.client.OrderServiceClient;
import com.microservice.order.messaging.OrderEventPublisher;
import com.microservice.order.model.Order;
import com.microservice.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceClient serviceClient;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository,
                        OrderServiceClient serviceClient,
                        OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.serviceClient = serviceClient;
        this.orderEventPublisher = orderEventPublisher;
    }

    public Order createOrder(Order order) {
        // Sync checks with Circuit Breaker + Retry via Resilience4j
        serviceClient.getUser(order.getUserId());
        serviceClient.getProduct(order.getProductId());

        order.setStatus(Order.OrderStatus.PENDING);
        Order saved = orderRepository.save(order);

        // Async: trigger inventory reservation in product-service via Kafka
        orderEventPublisher.publishOrderPlaced(saved);
        return saved;
    }

    public Order confirmOrder(Long id) {
        return orderRepository.findById(id)
            .map(order -> {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                return orderRepository.save(order);
            })
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Order cancelOrder(Long id) {
        return orderRepository.findById(id)
            .map(order -> {
                order.setStatus(Order.OrderStatus.CANCELLED);
                return orderRepository.save(order);
            })
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByProductId(Long productId) {
        return orderRepository.findByProductId(productId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long id, Order order) {
        return orderRepository.findById(id)
            .map(existing -> {
                existing.setQuantity(order.getQuantity());
                existing.setTotalPrice(order.getTotalPrice());
                existing.setStatus(order.getStatus());
                return orderRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
