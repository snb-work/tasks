package com.example.tasks.service;

import com.example.tasks.exception.ResourceNotFoundException;
import com.example.tasks.dto.CreateTaskRequest;
import com.example.tasks.dto.UpdateTaskRequest;
import com.example.tasks.model.Task;
import com.example.tasks.model.TaskStatus;
import com.example.tasks.model.User;
import com.example.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepo;
    private final UserService userService;
    
    @Transactional
    public Task createTask(CreateTaskRequest request) {
        log.debug("Creating task for user ID: {}", request.getUserId());
        
        // Get the user
        User user = userService.getUserById(request.getUserId());
        if (user == null) {
            log.error("User not found with ID: {}", request.getUserId());
            throw new ResourceNotFoundException("User", "id", request.getUserId());
        }

        // Create and save new task
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .user(user)
                .build();
        
        return taskRepo.save(task);
    }
    
    @Transactional(readOnly = true)
    public Page<Task> getTasksByUserId(Integer userId, Pageable pageable) {
        log.debug("Fetching tasks for user ID: {}", userId);
        
        // Verify user exists
        if (!userService.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User", "id", userId);
        }
            
        return taskRepo.findByUserUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Task> getAllTasks(Pageable pageable) {
        log.debug("Fetching all tasks");
        return taskRepo.findAll(pageable);
    }

    @Transactional
    public Task updateTask(Integer taskId, UpdateTaskRequest request) {
        log.info("Updating task ID: {} with request", taskId);
        
        // 1. Find existing task
        Task existingTask = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        
        // 2. Update user if specified in the request
        if (request.getUserId() != null) {
            User user = userService.getUserById(request.getUserId());
            existingTask.setUser(user);
        }
        
        // 3. Update allowed fields if they are not null
        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            existingTask.setStatus(request.getStatus());
        }
        
        // 4. Save and return
        return taskRepo.save(existingTask);
    }

    @Transactional
    public void deleteTask(Integer id) {
        log.debug("Deleting task ID: {}", id);
        
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new ResourceNotFoundException("Task", "id", id);
                });
                
        taskRepo.delete(task);
    }
}
