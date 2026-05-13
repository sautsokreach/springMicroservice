package com.microservice.order.messaging;

import com.microservice.order.messaging.dto.InventoryUpdate;
import com.microservice.order.model.Order;
import com.microservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InventoryUpdateConsumer {

    private final OrderService orderService;
    private final OrderNotificationPublisher orderNotificationPublisher;

    @Bean
    public Consumer<InventoryUpdate> processInventoryUpdate() {
        return update -> {
            log.info("Received inventory update: orderId={}, productId={}, status={}, newStock={}",
                update.getOrderId(), update.getProductId(), update.getStatus(), update.getNewStock());

            if ("RESERVED".equals(update.getStatus())) {
                Order confirmed = orderService.confirmOrder(update.getOrderId());
                orderNotificationPublisher.publishOrderNotification(confirmed);
            } else if ("INSUFFICIENT".equals(update.getStatus())) {
                Order cancelled = orderService.cancelOrder(update.getOrderId());
                orderNotificationPublisher.publishOrderNotification(cancelled);
                log.warn("Order {} cancelled due to insufficient inventory for product {}",
                    update.getOrderId(), update.getProductId());
            }
        };
    }
}
