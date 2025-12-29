package com.calendarCO.services;

import com.calendarCO.clients.UserServiceClient;
import com.calendarCO.models.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserIntegrationService {

    private final UserServiceClient userServiceClient;

    @Cacheable(value = "users", key = "#username")
    public UserDTO getUserByUsername(String username) {
        try {
            return userServiceClient.getUserByUsername(username);
        } catch (Exception e) {
            log.error("Error fetching user by username: {}", username, e);
            throw new RuntimeException("User service unavailable", e);
        }
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO getUserById(Long id) {
        try {
            return userServiceClient.getUserById(id);
        } catch (Exception e) {
            log.error("Error fetching user by id: {}", id, e);
            throw new RuntimeException("User service unavailable", e);
        }
    }

    public List<UserDTO> getAllUsers() {
        try {
            return userServiceClient.getAllUsers();
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            throw new RuntimeException("User service unavailable", e);
        }
    }
}
