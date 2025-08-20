package com.example.tasks.dto;

import com.example.tasks.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTaskRequest {
    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;
    
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    private TaskStatus status;
}
