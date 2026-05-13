package com.microservice.product.messaging;

import com.microservice.product.messaging.dto.OrderEvent;
import com.microservice.product.model.Product;
import com.microservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final ProductService productService;
    private final InventoryUpdatePublisher inventoryUpdatePublisher;

    @Bean
    public Consumer<OrderEvent> processOrderEvent() {
        return event -> {
            log.info("Processing ORDER_PLACED event: orderId={}, productId={}, qty={}",
                event.getOrderId(), event.getProductId(), event.getQuantity());
            try {
                Product updated = productService.reserveInventory(
                    event.getProductId(), event.getQuantity());
                inventoryUpdatePublisher.publishInventoryUpdate(
                    event.getOrderId(), event.getProductId(),
                    event.getQuantity(), updated.getQuantity(), "RESERVED");
            } catch (RuntimeException e) {
                log.warn("Inventory reservation failed for orderId={}: {}",
                    event.getOrderId(), e.getMessage());
                inventoryUpdatePublisher.publishInventoryUpdate(
                    event.getOrderId(), event.getProductId(),
                    event.getQuantity(), 0, "INSUFFICIENT");
            }
        };
    }
}
