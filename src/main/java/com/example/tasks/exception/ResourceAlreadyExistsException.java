package com.example.tasks.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    
    /**
     * Constructs a new ResourceAlreadyExistsException.
     *
     * @param resourceName The name of the resource type (e.g., "User", "Task")
     * @param fieldName    The name of the field that caused the conflict
     * @param fieldValue   The value of the field that caused the conflict
     */
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
