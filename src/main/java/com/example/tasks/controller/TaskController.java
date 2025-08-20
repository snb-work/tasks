package com.example.tasks.controller;

import com.example.tasks.dto.CreateTaskRequest;
import com.example.tasks.model.Task;
import com.example.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import com.example.tasks.dto.UpdateTaskRequest;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    @GetMapping
    public Page<Task> getTasks(
            @RequestParam(required = false) Integer userId,
            Pageable pageable) {
        if (userId != null) {
            return taskService.getTasksByUserId(userId, pageable);
        }
        return taskService.getAllTasks(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTaskRequest request) {
        Task updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Task deleted successfully"));
    }
}
