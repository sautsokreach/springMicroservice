package com.microservice.user.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderNotification {
    private Long orderId;
    private Long userId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
