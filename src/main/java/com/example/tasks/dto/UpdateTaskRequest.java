package com.example.tasks.dto;

import com.example.tasks.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating task details.
 * All fields are optional - only provided fields will be updated.
 */
@Data
public class UpdateTaskRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;
    
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    private TaskStatus status;
    
    /**
     * ID of the user to assign this task to.
     * If provided, will update the task's assigned user.
     */
    private Integer userId;
}
