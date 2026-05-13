package com.microservice.user.service;

import com.microservice.user.messaging.UserEventPublisher;
import com.microservice.user.model.User;
import com.microservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public UserService(UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    public User createUser(User user) {
        User saved = userRepository.save(user);
        userEventPublisher.publishUserRegistered(saved);
        return saved;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
            .map(existing -> {
                existing.setEmail(user.getEmail());
                existing.setFirstName(user.getFirstName());
                existing.setLastName(user.getLastName());
                existing.setPassword(user.getPassword());
                User saved = userRepository.save(existing);
                userEventPublisher.publishUserUpdated(saved);
                return saved;
            })
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        userEventPublisher.publishUserDeleted(id);
    }
}
