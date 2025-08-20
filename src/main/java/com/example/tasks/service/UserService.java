package com.example.tasks.service;

import com.example.tasks.exception.ResourceAlreadyExistsException;
import com.example.tasks.exception.ResourceNotFoundException;
import com.example.tasks.exception.UserInactiveException;
import com.example.tasks.model.User;
import com.example.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    @Transactional
    public User createUser(User user) {
        log.info("Creating user: {}", user.getUsername());
        
        // Check for existing username (including soft-deleted users)
        Optional<User> existingByUsername = userRepo.findByUsername(user.getUsername());
        if (existingByUsername.isPresent()) {
            User existingUser = existingByUsername.get();
            if ("N".equals(existingUser.getIsActive())) {
                // Reactivate the soft-deleted user
                existingUser.setIsActive("Y");
                existingUser.setEmail(user.getEmail()); // Update email if changed
                existingUser.setFullName(user.getFullName());
                userRepo.save(existingUser);
                log.info("Reactivated soft-deleted user with username: {}", user.getUsername());
                return existingUser;
            } else {
                log.warn("Username already in use: {}", user.getUsername());
                throw new ResourceAlreadyExistsException("User", "username", user.getUsername());
            }
        }
        
        // Check for existing email (including soft-deleted users)
        Optional<User> existingByEmail = userRepo.findByEmail(user.getEmail());
        if (existingByEmail.isPresent()) {
            User existingUser = existingByEmail.get();
            if ("N".equals(existingUser.getIsActive())) {
                // Reactivate the soft-deleted user
                existingUser.setIsActive("Y");
                existingUser.setUsername(user.getUsername());
                existingUser.setFullName(user.getFullName());
                userRepo.save(existingUser);
                log.info("Reactivated soft-deleted user with email: {}", user.getEmail());
                return existingUser;
            } else {
                log.warn("Email already in use by another active user: {}", user.getEmail());
                throw new ResourceAlreadyExistsException("User", "email", user.getEmail());
            }
        }
        
        // Set default active status for new users
        user.setIsActive("Y");
        User savedUser = userRepo.save(user);
        log.info("User created successfully with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(boolean includeInactive) {
        if (includeInactive) {
            log.debug("Fetching all users including inactive ones");
            return userRepo.findAll();
        } else {
            log.debug("Fetching only active users");
            List<User> users = userRepo.findByIsActive("Y");
            log.debug("Successfully retrieved {} active users", users.size());
            return users;
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        log.debug("Fetching active user by ID: {}", id);
        User user = userRepo.findByUserIdAndIsActive(id, "Y")
                .orElseThrow(() -> {
                    log.error("Active user not found with ID: {}", id);
                    return new ResourceNotFoundException("User", "id", id);
                });
        log.debug("Retrieved active user with ID: {}", id);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.debug("Fetching active user by username: {}", username);
        User user = userRepo.findByUsernameAndIsActive(username, "Y")
                .orElseThrow(() -> {
                    log.error("Active user not found with username: {}", username);
                    return new ResourceNotFoundException("User", "username", username);
                });
        log.debug("Retrieved active user with username: {}", username);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        log.debug("Fetching active user by email: {}", email);
        User user = userRepo.findByEmailAndIsActive(email, "Y")
                .orElseThrow(() -> {
                    log.error("Active user not found with email: {}", email);
                    return new ResourceNotFoundException("User", "email", email);
                });
        log.debug("Retrieved active user with email: {}", email);
        return user;
    }

    @Transactional
    public User updateUserById(Integer id, User updatedUser) {
        log.info("Updating user ID: {}", id);
        
        // 1. Find existing user
        User existingUser = userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // 2. Check for duplicate username (if username is being updated)
        if (!existingUser.getUsername().equals(updatedUser.getUsername()) && 
            userRepo.existsByUsername(updatedUser.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", updatedUser.getUsername());
        }
        
        // 3. Check for duplicate email (if email is being updated)
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && 
            userRepo.existsByEmail(updatedUser.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", updatedUser.getEmail());
        }
        
        // 4. Update only the allowed fields
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFullName(updatedUser.getFullName());
        
        // 5. Save and return
        return userRepo.save(existingUser);
    }

    @Transactional
    public void deleteUserById(Integer id) {
        log.info("Soft deleting user ID: {}", id);
        User user = userRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found for deletion, ID: {}", id);
                    return new ResourceNotFoundException("User", "id", id);
                });
        
        // Check if user is already inactive
        if ("N".equals(user.getIsActive())) {
            log.warn("User is already inactive, ID: {}", id);
            throw new UserInactiveException("User is already inactive");
        }
        
        // Soft delete by setting isActive to 'N'
        user.setIsActive("N");
        userRepo.save(user);
        log.info("User soft deleted successfully, ID: {}", id);
    }

    public boolean existsById(Integer id) {
        return userRepo.findByUserIdAndIsActive(id, "Y").isPresent();
    }
}
