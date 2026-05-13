package com.microservice.user.messaging;

import com.microservice.user.messaging.dto.OrderNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class OrderNotificationConsumer {

    @Bean
    public Consumer<OrderNotification> handleOrderNotification() {
        return notification -> {
            log.info("Received order notification: orderId={}, userId={}, status={}, message={}",
                notification.getOrderId(),
                notification.getUserId(),
                notification.getStatus(),
                notification.getMessage());
            // TODO: dispatch email / push notification to user
        };
    }
}
