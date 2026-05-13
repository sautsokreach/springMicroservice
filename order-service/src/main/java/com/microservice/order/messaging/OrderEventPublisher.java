package com.microservice.order.messaging;

import com.microservice.order.messaging.dto.OrderEvent;
import com.microservice.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final StreamBridge streamBridge;

    public void publishOrderPlaced(Order order) {
        OrderEvent event = OrderEvent.builder()
            .orderId(order.getId())
            .userId(order.getUserId())
            .productId(order.getProductId())
            .quantity(order.getQuantity())
            .status("ORDER_PLACED")
            .timestamp(LocalDateTime.now())
            .build();
        boolean sent = streamBridge.send("orderEvents-out-0", event);
        log.info("Published ORDER_PLACED event for orderId={}, sent={}", order.getId(), sent);
    }
}
