package com.example.tasks.repository;

import com.example.tasks.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    
    /**
     * Find all tasks for a specific user with pagination.
     *
     * @param userId   The ID of the user
     * @param pageable Pagination information
     * @return Page of tasks
     */
    Page<Task> findByUserUserId(Integer userId, Pageable pageable);
}
