package com.microservice.order.messaging;

import com.microservice.order.messaging.dto.OrderNotificationMsg;
import com.microservice.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderNotificationPublisher {

    private final StreamBridge streamBridge;

    public void publishOrderNotification(Order order) {
        OrderNotificationMsg msg = OrderNotificationMsg.builder()
            .orderId(order.getId())
            .userId(order.getUserId())
            .status(order.getStatus().name())
            .message("Your order #" + order.getId() + " has been " + order.getStatus().name().toLowerCase())
            .timestamp(LocalDateTime.now())
            .build();
        boolean sent = streamBridge.send("orderNotifications-out-0", msg);
        log.info("Published order notification for orderId={}, status={}, sent={}",
            order.getId(), order.getStatus(), sent);
    }
}
