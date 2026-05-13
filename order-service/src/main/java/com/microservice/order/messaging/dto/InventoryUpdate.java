package com.microservice.order.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryUpdate {
    private Long orderId;
    private Long productId;
    private Integer quantityReserved;
    private Integer newStock;
    private String status;
    private LocalDateTime timestamp;
}
