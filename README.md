# Todo List Application

A Spring Boot application for managing todo items with MSSQL database, containerized with Docker and Nginx.

## Prerequisites

- Docker and Docker Compose
- Java 11 or higher
- Maven

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

3. **Run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

   This will start:
   - MSSQL Server on port 1433
   - Spring Boot application on port 8080
   - Nginx reverse proxy on port 80

## API Endpoints

- `GET /api/todos` - Get all todos with pagination (10 items per page)
  - Query params: `page`, `size`, `sortBy`, `sortDir`
  - Example: `GET /api/todos?page=0&size=10&sortBy=createdAt&sortDir=desc`

- `GET /api/todos/{id}` - Get a todo by ID
- `POST /api/todos` - Create a new todo
- `PUT /api/todos/{id}` - Update a todo
- `DELETE /api/todos/{id}` - Delete a todo

## Example Requests

### Create a Todo
```http
POST /api/todos
Content-Type: application/json

{
    "title": "Complete project",
    "description": "Finish the todo list application",
    "completed": false
}
```

### Get Paginated Todos
```http
GET /api/todos?page=0&size=10&sortBy=createdAt&sortDir=desc
```

## Database
- The application uses MSSQL Server running in a Docker container
- Database name: `TESTDB`
- Username: `sa`
- Password: `yourStrong(!)Password`

## Development

### Running Tests
```bash
mvn test
```

### Accessing the Database
You can connect to the MSSQL database using any SQL client with these details:
- Host: localhost
- Port: 1433
- Database: TESTDB
- Username: sa
- Password: yourStrong(!)Password

## License
This project is licensed under the MIT License.
