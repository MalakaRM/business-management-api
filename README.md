# Smart Business Management System

A RESTful business operations and inventory management system built with Spring Boot, PostgreSQL, Spring Security, and JWT authentication.

## Overview

The Smart Business Management System is a backend application designed to manage core business operations such as products, inventory, suppliers, purchases, customers, orders, sales, payments, invoices, reporting, and audit logs.

The system follows a layered architecture and provides secure REST APIs with role-based and permission-based access control.
## Features

### Authentication & Authorization
- User registration and login
- JWT-based authentication
- BCrypt password encryption
- Role-based access control
- Permission-based authorization
- Default ADMIN, MANAGER, and EMPLOYEE roles

### Product & Category Management
- Create and manage product categories
- Create and update products
- Unique SKU management
- Product activation/deactivation
- Product search and retrieval

### Inventory Management
- Real-time stock tracking
- Stock increase, deduction, and adjustment
- Stock movement history
- Low-stock monitoring
- Insufficient stock validation

### Supplier & Purchase Management
- Supplier management
- Purchase creation
- Purchase receiving
- Purchase cancellation
- Automatic inventory updates when purchases are received

### Customer & Order Management
- Customer management
- Order creation
- Order confirmation and cancellation
- Automatic stock deduction when orders are confirmed
- Stock restoration when confirmed orders are cancelled

### POS & Billing
- POS sales processing
- Payment recording
- Invoice generation
- Automatic inventory updates
- Transaction-based sale processing

### Reports & Dashboard
- Dashboard summary
- Sales reports
- Purchase reports
- Inventory reports
- Low-stock reports

### Audit Logging
- Track important system actions
- Record user, action, entity, and timestamp
- Audit logs for key business operations

### API & Development
- RESTful API architecture
- Bean Validation
- Global exception handling
- Swagger / OpenAPI documentation
- Dockerized application
- PostgreSQL database
- Unit testing with JUnit and Mockito

  ## Tech Stack

### Backend
- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Hibernate
- RESTful APIs
- Bean Validation
- Lombok

### Database
- PostgreSQL 18

### Authentication & Security
- JWT (JSON Web Token)
- BCrypt Password Hashing
- Role-Based Access Control (RBAC)
- Permission-Based Authorization

### API Documentation
- Swagger / OpenAPI

### Testing
- JUnit
- Mockito

### DevOps & Tools
- Docker
- Docker Compose
- Maven
- Git
- GitHub

  ## System Architecture

The application follows a layered architecture to separate responsibilities and maintain a clean, maintainable codebase.

### Application Architecture

    Client / API Consumer
            |
            | HTTP Request
            v
    +-----------------------+
    |      Controller       |
    |   REST API Layer      |
    +-----------------------+
            |
            v
    +-----------------------+
    |       Service         |
    |   Business Logic      |
    +-----------------------+
            |
            v
    +-----------------------+
    |      Repository       |
    |   Data Access Layer   |
    +-----------------------+
            |
            v
    +-----------------------+
    |      PostgreSQL       |
    |       Database        |
    +-----------------------+

### Security Flow

    HTTP Request
          |
          v
    JWT Authentication Filter
          |
          v
    Security Context
          |
          v
    Controller
          |
          v
    Service
          |
          v
    Repository

The application uses stateless JWT authentication. After successful login, the client receives a JWT token and sends it with subsequent protected requests using the `Authorization: Bearer <token>` header.

### Container Architecture

    Docker Compose
          |
          +-----------------------+
          |                       |
          v                       v
    +------------------+   +------------------+
    | Spring Boot      |   | PostgreSQL 18    |
    | Backend          |-->| Database         |
    | Port: 8080       |   | Port: 5432       |
    +------------------+   +------------------+
          |
          v
       REST API

PostgreSQL data is persisted using a Docker volume, allowing database data to survive container recreation.
## Project Structure

```text
src/
└── main/
    └── java/
        └── com/
            └── smartbusiness/
                └── businessmanagement/
                    ├── config/
                    │
                    ├── controller/
                    │
                    ├── dto/
                    │   ├── request/
                    │   │   └── update/
                    │   └── response/
                    │
                    ├── entity/
                    │   └── enum/
                    │
                    ├── advicer/
                    │
                    ├── mapper/
                    │
                    ├── repository/
                    │
                    └── service/
                        ├── impl/
                        └── security/
