package com.example.tasks.repository;

import com.example.tasks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByIsActive(String isActive);
    Optional<User> findByUserIdAndIsActive(Integer userId, String isActive);
    Optional<User> findByUsernameAndIsActive(String username, String isActive);
    Optional<User> findByEmailAndIsActive(String email, String isActive);
    boolean existsByUsernameAndIsActive(String username, String isActive);
    boolean existsByEmailAndIsActive(String email, String isActive);
}
