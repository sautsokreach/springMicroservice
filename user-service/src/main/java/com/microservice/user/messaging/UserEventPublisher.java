package com.microservice.user.messaging;

import com.microservice.user.messaging.dto.UserEvent;
import com.microservice.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final StreamBridge streamBridge;

    public void publishUserRegistered(User user) {
        publish(user, "USER_REGISTERED");
    }

    public void publishUserUpdated(User user) {
        publish(user, "USER_UPDATED");
    }

    public void publishUserDeleted(Long userId) {
        UserEvent event = UserEvent.builder()
            .userId(userId)
            .eventType("USER_DELETED")
            .timestamp(LocalDateTime.now())
            .build();
        boolean sent = streamBridge.send("userEvents-out-0", event);
        log.info("Published USER_DELETED event for userId={}, sent={}", userId, sent);
    }

    private void publish(User user, String eventType) {
        UserEvent event = UserEvent.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .eventType(eventType)
            .timestamp(LocalDateTime.now())
            .build();
        boolean sent = streamBridge.send("userEvents-out-0", event);
        log.info("Published {} event for userId={}, sent={}", eventType, user.getId(), sent);
    }
}
