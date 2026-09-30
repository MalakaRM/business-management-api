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
## Authentication & Authorization

The application uses Spring Security with JWT-based stateless authentication.

### Authentication Flow

```text
User
 |
 | Username + Password
 v
POST /api/auth/login
 |
 v
AuthenticationManager
 |
 v
CustomUserDetailsService
 |
 v
BCrypt Password Verification
 |
 v
JWT Token Generation
 |
 v
Client

User
  |
  | Many-to-Many
  v
Role
  |
  | Many-to-Many
  v
Permission

## Main Modules

### 1. Product & Category Management

Manages product categories and product information including SKU, pricing, status, and category relationships.

### 2. Inventory Management

Tracks current stock levels and maintains a history of stock movements such as purchases, sales, damages, and manual adjustments.

### 3. Supplier & Purchase Management

Manages suppliers and purchase transactions. Receiving a purchase automatically increases the corresponding inventory stock.

### 4. Customer & Order Management

Manages customers and sales orders with order status handling, stock validation, stock deduction, and stock restoration when applicable.

### 5. POS & Billing

Processes sales transactions, records payments, generates invoices, and updates inventory within a transactional workflow.

### 6. Reports & Dashboard

Provides business information including sales, purchases, inventory status, low-stock items, and dashboard summaries.

### 7. Audit Logging

Records important system activities including the user, action, affected entity, entity ID, description, and timestamp.

## Getting Started

### Prerequisites

Make sure the following are installed:

- Java 21
- Docker
- Docker Compose
- Git

### Environment Variables

Create a `.env` file in the project root directory.

Example:

```env
POSTGRES_DB=business_management
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_database_password
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=3600000

## API Overview

The application exposes RESTful APIs for the following business operations:

| Module | Endpoint | Description |
|---|---|---|
| Authentication | `/api/auth` | User registration and login |
| Products | `/api/products` | Product management |
| Categories | `/api/categories` | Category management |
| Inventory | `/api/inventory` | Stock management and stock movements |
| Suppliers | `/api/suppliers` | Supplier management |
| Purchases | `/api/purchases` | Purchase management |
| Customers | `/api/customers` | Customer management |
| Orders | `/api/orders` | Order management |
| POS | `/api/pos` | Sales and billing |
| Reports | `/api/reports` | Business reports |
| Dashboard | `/api/dashboard` | Dashboard summaries |
| Audit | `/api/audit` | Audit log access |

Most business endpoints require JWT authentication.

API requests can be tested using Swagger UI or an API client such as Postman.

## Testing

The application uses JUnit and Mockito for unit testing.

### Testing Approach

Service-layer business logic is tested using:

- JUnit 5
- Mockito
- Spring Boot Test

Unit tests focus on validating business rules and service behavior without requiring a real database.

### Current Test Coverage

The current test suite includes unit tests for product management services, including:

- Product creation
- Duplicate SKU validation
- Business rule validation

Additional tests can be added as the application evolves.

## Future Improvements

The following improvements can be considered for future versions of the system:

- Angular-based frontend application
- Role and permission management UI
- Advanced reporting and analytics
- Redis caching
- Real-time notifications using WebSocket
- Automated CI/CD pipeline
- Improved test coverage across all service modules
- Production-ready deployment and cloud infrastructure

## Project Status

The backend implementation is complete and includes the core business operations, authentication, authorization, inventory management, purchasing, sales, billing, reporting, and audit logging features.

The project is currently maintained as a backend-focused portfolio project and is available as a professional GitHub repository.

The Angular frontend is planned as a future improvement.

## License

This project is developed for educational and portfolio purposes.
