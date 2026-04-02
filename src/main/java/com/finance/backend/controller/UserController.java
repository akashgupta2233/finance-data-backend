package com.finance.backend.controller;

import com.finance.backend.dto.UserDto;
import com.finance.backend.entity.User;
import com.finance.backend.exception.ResourceNotFoundException;
import com.finance.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto request) {
        User user = toEntity(request);
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @GetMapping
    public List<UserDto> listUsers() {
        return userService.listUsers().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(toDto(user)))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable Long id) {
        User user = userService.activateUser(id);
        return ResponseEntity.ok(toDto(user));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable Long id) {
        User user = userService.deactivateUser(id);
        return ResponseEntity.ok(toDto(user));
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setRoles(user.getRoles());
        return dto;
    }

    private User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setStatus(dto.getStatus());
        user.setRoles(dto.getRoles());
        return user;
    }
}
