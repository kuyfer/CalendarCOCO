package com.calendarCO.clients;

import com.calendarCO.models.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", path = "/api")
public interface UserServiceClient {

    @GetMapping("/users")
    List<UserDTO> getAllUsers();

    @GetMapping("/users/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/users/id/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
