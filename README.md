# Task Management API

A Spring Boot application for managing tasks and users with MSSQL database.

## Table of Contents
- [Features](#features)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
  - [Users](#users)
  - [Tasks](#tasks)
- [Error Responses](#error-responses)
- [Configuration](#configuration)

## Features
- Task CRUD operations
- User management with soft delete
- Pagination

## Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- MSSQL Server 2019+

1. Copy `.env.example` to `.env`
   ```bash
   cp .env.example .env
   ```
2. Update database credentials in `.env`
3. Create database in MSSQL Server

## Running the Application

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run
```

Access at: `http://localhost:8080`

## Database Schema

```sql
CREATE SCHEMA Tasks;
GO

CREATE TABLE Tasks.users (
    user_id INT IDENTITY(1, 1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    full_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    is_active CHAR(1),
    created_at DATETIME2(6),
    updated_at DATETIME2(6)
);

CREATE TABLE Tasks.tasks (
    task_id INT IDENTITY(1, 1) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    status NVARCHAR(20),
    user_id INT NOT NULL,
    created_at DATETIME2(6),
    updated_at DATETIME2(6),
    CONSTRAINT FK_tasks_users FOREIGN KEY (user_id)
        REFERENCES Tasks.users(user_id)
);
```

## Configuration

### Environment Variables
- `DB_URL`: Database URL
- `DB_USERNAME`: Database user
- `DB_PASSWORD`: Database password

### Application
- Port: 8080
- Hibernate: update
- SQL Logging: enabled

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd tasks
   ```

2. **Build the application**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The application will be available at `http://localhost:8080`

## API Endpoints

### Users
- `POST /api/users` - Create a new user
  - Request body: User details (username, fullName, email)
  - Returns: Created user with HTTP 201
  - Example:
    ```json
    {
        "username": "john.doe",
        "fullName": "John Doe",
        "email": "john.doe@example.com"
    }
    ```

- `GET /api/users` - Get all users
  - Query params:
    - `includeInactive` (boolean, optional): Include inactive users (default: false)
  - Returns: List of users
  - Example: `GET /api/users?includeInactive=true`

- `GET /api/users/{id}` - Get user by ID
  - Path variable: User ID
  - Returns: User details
  - Example: `GET /api/users/1`

- `PUT /api/users/{id}` - Update a user
  - Path variable: User ID
  - Request body: User details (username, fullName, email)
  - Returns: Updated user
  - Example:
    ```json
    {
        "username": "jane.doe",
        "fullName": "Jane Doe",
        "email": "jane.doe@example.com"
    }
    ```

- `DELETE /api/users/{id}` - Soft delete a user
  - Path variable: User ID
  - Operation: Sets the user's `is_active` flag to 'N' instead of hard deletion
  - Returns: Success message
  - Example: `DELETE /api/users/1`

- `GET /api/users/external` - Get users from external API
  - Returns: List of external users
  - Example: `GET /api/users/external`

### Tasks
- `POST /api/tasks` - Create a new task
  - Request body: Task details (userId, title, description, status)
  - Returns: Created task with HTTP 201
  - Example:
    ```json
    {
        "userId": 1,
        "title": "Complete project",
        "description": "Finish the tasks API",
        "status": "TODO"
    }
    ```

- `GET /api/tasks` - Get tasks with pagination
  - Query params:
    - `userId` (integer, optional): Filter tasks by user ID
    - `page` (integer, optional): Page number (0-based, default: 0)
  - Returns: Paginated list of tasks
  - Example: `GET /api/tasks?userId=1&page=0`

- `PUT /api/tasks/{id}` - Update a task
  - Path variable: Task ID
  - Request body: Updated task details (title, description, status)
  - Returns: Updated task
  - Example:
    ```json
    {
        "title": "Updated task title",
        "description": "Updated description",
        "status": "IN_PROGRESS"
    }
    ```

- `DELETE /api/tasks/{id}` - Delete a task
  - Path variable: Task ID
  - Returns: Success message
  - Example: `DELETE /api/tasks/1`

## Error Responses

Common error responses include:

- `400 Bad Request`: Invalid request data or missing required fields
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server-side error

Example error response:
```json
{
    "timestamp": "2025-08-20T04:56:09.000+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "User not found with id: 999",
    "path": "/api/users/999"
}
```

## Database Configuration
- Database: MSSQL Server
- Schema: `Tasks`
- Username: `your_username`
- Password: `your_password`
- Host: `localhost`
- Port: `1433`
