package com.example.tasks.controller;

import com.example.tasks.dto.ExternalUserDto;
import com.example.tasks.model.User;
import com.example.tasks.service.ExternalApiService;
import com.example.tasks.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ExternalApiService externalApiService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false, defaultValue = "false") boolean includeInactive) {
        List<User> users = userService.getAllUsers(includeInactive);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/external")
    public ResponseEntity<List<ExternalUserDto>> getExternalUsers() {
        return ResponseEntity.ok(externalApiService.getExternalUsers());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(
            @PathVariable Integer id, 
            @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUserById(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully"));
    }
}