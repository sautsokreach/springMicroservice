package com.microservice.order.service;

import com.microservice.order.client.OrderServiceClient;
import com.microservice.order.model.Order;
import com.microservice.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceClient serviceClient;

    public OrderService(OrderRepository orderRepository, OrderServiceClient serviceClient) {
        this.orderRepository = orderRepository;
        this.serviceClient = serviceClient;
    }

    public Order createOrder(Order order) {
        // Circuit Breaker + Retry protect these calls
        serviceClient.getUser(order.getUserId());
        serviceClient.getProduct(order.getProductId());
        return orderRepository.save(order);
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
                .map(existingOrder -> {
                    existingOrder.setQuantity(order.getQuantity());
                    existingOrder.setTotalPrice(order.getTotalPrice());
                    existingOrder.setStatus(order.getStatus());
                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
