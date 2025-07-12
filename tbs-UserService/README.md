# Travel Booking System - User Service

This is the User Service microservice for the Travel Booking System. It handles user management, authentication, and profile management.

## Prerequisites

1. Java 17 or higher
2. Spring Tool Suite (STS) 4.x
3. MySQL 8.0 or higher
4. Postman
5. Maven

## Setup Instructions

### 1. Database Setup

1. Open MySQL command line or MySQL Workbench
2. Create the database:
```sql
CREATE DATABASE travel_booking;
```

### 2. Project Setup in STS

1. Open Spring Tool Suite
2. Go to File -> Import
3. Select "Existing Maven Projects"
4. Browse to the extracted project folder
5. Select the project and click "Finish"
6. Wait for Maven to download all dependencies

### 3. Configure Database Connection

1. Open `src/main/resources/application.properties`
2. Update the database credentials if needed:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Run the Application

1. Right-click on the project in STS
2. Select "Run As" -> "Spring Boot App"
3. Wait for the application to start
4. The service will run on port 8081

## Testing with Postman

### 1. Create User
```
POST http://localhost:8081/api/users
Content-Type: application/json

{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "role": "CUSTOMER"
}
```

### 2. Get User by ID
```
GET http://localhost:8081/api/users/1
```

### 3. Update User
```
PUT http://localhost:8081/api/users/1
Content-Type: application/json

{
    "username": "testuser",
    "email": "updated@example.com",
    "firstName": "Updated",
    "lastName": "User",
    "role": "CUSTOMER"
}
```

### 4. Search Users
```
GET http://localhost:8081/api/users/search
```

### 5. Delete User
```
DELETE http://localhost:8081/api/users/1
```

## Project Structure

```
src/main/java/com/travel/userservice/
├── config/
│   └── ModelMapperConfig.java
├── controller/
│   └── UserController.java
├── dto/
│   ├── UserDto.java
│   ├── AuthDto.java
│   ├── UserProfileDto.java
│   └── UserSearchDto.java
├── entity/
│   └── User.java
├── exception/
│   ├── ValidationExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/
│   └── UserRepository.java
└── service/
    └── UserService.java
```

## Common Issues and Solutions

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in application.properties
   - Ensure database 'travel_booking' exists

2. **Port Already in Use**
   - Change server.port in application.properties
   - Or kill the process using port 8081

3. **Maven Dependencies**
   - Right-click project -> Maven -> Update Project
   - Clean and rebuild project

## API Documentation

### User Endpoints

1. **Create User**
   - POST /api/users
   - Creates a new user

2. **Get User**
   - GET /api/users/{id}
   - Retrieves user by ID

3. **Update User**
   - PUT /api/users/{id}
   - Updates user information

4. **Delete User**
   - DELETE /api/users/{id}
   - Deletes a user

5. **Search Users**
   - GET /api/users/search
   - Searches users with optional filters 