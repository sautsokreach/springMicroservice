package com.microservice.product.messaging;

import com.microservice.product.messaging.dto.InventoryUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryUpdatePublisher {

    private final StreamBridge streamBridge;

    public void publishInventoryUpdate(Long orderId, Long productId,
                                        int quantityReserved, int newStock, String status) {
        InventoryUpdate update = InventoryUpdate.builder()
            .orderId(orderId)
            .productId(productId)
            .quantityReserved(quantityReserved)
            .newStock(newStock)
            .status(status)
            .timestamp(LocalDateTime.now())
            .build();
        boolean sent = streamBridge.send("inventoryUpdates-out-0", update);
        log.info("Published inventory update: orderId={}, productId={}, status={}, newStock={}, sent={}",
            orderId, productId, status, newStock, sent);
    }
}
