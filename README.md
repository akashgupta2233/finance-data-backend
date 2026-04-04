# Finance Backend - Spring Boot Project

## Project Overview

Finance Backend is a Spring Boot application designed for managing financial data through secure, RESTful APIs. The application uses JWT-based authentication and follows a layered architecture (Controller → Service → Repository) to ensure scalability, maintainability, and clean separation of concerns.

---

## Technology Stack

* **Java**: 17
* **Spring Boot**: 3.2.4
* **Build Tool**: Maven
* **Database**: PostgreSQL
* **Key Dependencies**:

  * Spring Web
  * Spring Data JPA
  * Spring Security
  * Spring Validation
  * PostgreSQL Driver
  * Lombok

---

## Project Structure

```
finance-backend/
├── pom.xml
├── Dockerfile
├── README.md
└── src/
    ├── main/java/com/finance/backend/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── entity/
    │   ├── dto/
    │   ├── security/
    │   └── config/
    └── resources/
```

---

## Getting Started

### Prerequisites

* Java 17+
* Maven 3.6+
* PostgreSQL

### Setup & Run

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## Environment Configuration

Set the following environment variables:

```bash
DB_URL=<DB_URL>
DB_USERNAME=<DB_USERNAME>
DB_PASSWORD=<DB_PASSWORD>
SPRING_PROFILES_ACTIVE=dev
```

---

## API Documentation

All API endpoints are prefixed with `/api`. Include the JWT token in the Authorization header for protected endpoints:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## Authentication APIs

### Register User

**POST** `/api/auth/register`

Creates a new user account. Default role is VIEWER if no roles are specified.

**Request Body**

```json
{
  "username": "testuser",
  "email": "testuser@example.com",
  "password": "password123",
  "roles": ["ANALYST"]
}
```

**Response** (201 Created)

```json
{
  "id": 2,
  "username": "testuser",
  "email": "testuser@example.com",
  "status": "ACTIVE",
  "roles": ["ANALYST"]
}
```

**Validation**
- Username must be unique
- Email must be unique and valid format
- Password is required
- Valid roles: ADMIN, ANALYST, VIEWER

---

### Login

**POST** `/api/auth/login`

Authenticates a user and returns a JWT token.

**Request Body**

```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response** (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Error Responses**
- 401 Unauthorized: Invalid username or password
- 403 Forbidden: User account is inactive

---

## User Management APIs

All user management endpoints require ADMIN role.

### Create User

**POST** `/api/users`

**Authorization**: ADMIN only

Creates a new user with specified roles and status.

**Request Body**

```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "securepass123",
  "status": "ACTIVE",
  "roles": ["VIEWER", "ANALYST"]
}
```

**Response** (201 Created)

```json
{
  "id": 3,
  "username": "newuser",
  "email": "newuser@example.com",
  "status": "ACTIVE",
  "roles": ["VIEWER", "ANALYST"]
}
```

---

### List All Users

**GET** `/api/users`

**Authorization**: ADMIN only

Returns a list of all users in the system.

**Response** (200 OK)

```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "status": "ACTIVE",
    "roles": ["ADMIN"]
  },
  {
    "id": 2,
    "username": "testuser",
    "email": "testuser@example.com",
    "status": "ACTIVE",
    "roles": ["ANALYST"]
  }
]
```

---

### Get User by ID

**GET** `/api/users/{id}`

**Authorization**: ADMIN only

Retrieves a specific user by their ID.

**Response** (200 OK)

```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "status": "ACTIVE",
  "roles": ["ADMIN"]
}
```

**Error Responses**
- 404 Not Found: User not found with specified ID

---

### Get User by Username

**GET** `/api/users/{username}`

**Authorization**: ADMIN only

Retrieves a specific user by their username.

**Response** (200 OK)

```json
{
  "id": 2,
  "username": "testuser",
  "email": "testuser@example.com",
  "status": "ACTIVE",
  "roles": ["ANALYST"]
}
```

**Error Responses**
- 404 Not Found: User not found with specified username

---

### Activate User

**PUT** `/api/users/{id}/activate`

**Authorization**: ADMIN only

Activates a user account, allowing them to log in.

**Response** (200 OK)

```json
{
  "id": 2,
  "username": "testuser",
  "email": "testuser@example.com",
  "status": "ACTIVE",
  "roles": ["ANALYST"]
}
```

---

### Deactivate User

**PUT** `/api/users/{id}/deactivate`

**Authorization**: ADMIN only

Deactivates a user account, preventing them from logging in.

**Response** (200 OK)

```json
{
  "id": 2,
  "username": "testuser",
  "email": "testuser@example.com",
  "status": "INACTIVE",
  "roles": ["ANALYST"]
}
```

---

### Update User Roles

**PUT** `/api/users/{id}/roles`

**Authorization**: ADMIN only

Updates the roles assigned to a user.

**Request Body**

```json
{
  "roles": ["ADMIN", "ANALYST"]
}
```

**Response** (200 OK)

```json
{
  "id": 2,
  "username": "testuser",
  "email": "testuser@example.com",
  "status": "ACTIVE",
  "roles": ["ADMIN", "ANALYST"]
}
```

---

## Financial Record APIs

### Create Financial Record

**POST** `/api/financial-records`

**Authorization**: ADMIN only

Creates a new financial record.

**Request Body**

```json
{
  "amount": 1200.50,
  "description": "Monthly grocery shopping",
  "category": "Food",
  "type": "EXPENSE",
  "date": "2026-04-02"
}
```

**Response** (201 Created)

```json
{
  "id": 4,
  "amount": 1200.50,
  "type": "EXPENSE",
  "category": "Food",
  "date": "2026-04-02",
  "description": "Monthly grocery shopping"
}
```

**Validation**
- amount: Required, must be positive
- type: Required, must be INCOME or EXPENSE
- category: Required
- date: Required, ISO date format (YYYY-MM-DD)
- description: Optional

---

### List Financial Records

**GET** `/api/financial-records`

**Authorization**: VIEWER, ANALYST, or ADMIN

Retrieves financial records with optional filtering and pagination.

**Query Parameters**

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| startDate | Date | Filter records from this date (inclusive) | 2026-01-01 |
| endDate | Date | Filter records until this date (inclusive) | 2026-12-31 |
| category | String | Filter by category | Food |
| type | Enum | Filter by type (INCOME or EXPENSE) | EXPENSE |
| createdBy | String | Filter by username who created the record | admin |
| keyword | String | Search in description and category | grocery |
| page | Integer | Page number (0-indexed) | 0 |
| size | Integer | Number of records per page | 20 |
| sort | String | Sort field and direction | date,desc |

**Example Request**

```
GET /api/financial-records?startDate=2026-01-01&endDate=2026-12-31&category=Food&type=EXPENSE&page=0&size=20&sort=date,desc
```

**Response** (200 OK)

```json
{
  "content": [
    {
      "id": 4,
      "amount": 1200.50,
      "type": "EXPENSE",
      "category": "Food",
      "date": "2026-04-02",
      "description": "Monthly grocery shopping"
    },
    {
      "id": 3,
      "amount": 850.00,
      "type": "EXPENSE",
      "category": "Food",
      "date": "2026-03-15",
      "description": "Restaurant dinner"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 2,
  "size": 20,
  "number": 0,
  "empty": false
}
```

**Default Behavior**
- Default page size: 20
- Default sort: date descending, then id descending
- Date range validation: startDate must be before or equal to endDate

---

### Update Financial Record

**PUT** `/api/financial-records/{id}`

**Authorization**: ADMIN only

Updates an existing financial record.

**Request Body**

```json
{
  "amount": 1500.00,
  "description": "Updated grocery amount",
  "category": "Food & Dining",
  "type": "EXPENSE",
  "date": "2026-04-02"
}
```

**Response** (200 OK)

```json
{
  "id": 4,
  "amount": 1500.00,
  "type": "EXPENSE",
  "category": "Food & Dining",
  "date": "2026-04-02",
  "description": "Updated grocery amount"
}
```

**Error Responses**
- 404 Not Found: Financial record not found with specified ID

---

### Delete Financial Record

**DELETE** `/api/financial-records/{id}`

**Authorization**: ADMIN only

Soft deletes a financial record (marks as deleted, doesn't physically remove).

**Response** (204 No Content)

**Error Responses**
- 404 Not Found: Financial record not found with specified ID

---

### Get Financial Summary

**GET** `/api/financial-records/summary`

**Authorization**: VIEWER, ANALYST, or ADMIN

Retrieves a summary of all financial records including totals by category and recent activity.

**Response** (200 OK)

```json
{
  "totalIncome": 50000.00,
  "totalExpense": 32000.00,
  "netBalance": 18000.00,
  "categoryTotals": {
    "Salary": 50000.00,
    "Food": -12000.00,
    "Transportation": -8000.00,
    "Utilities": -5000.00,
    "Entertainment": -7000.00
  },
  "recentActivity": [
    {
      "id": 10,
      "amount": 1200.50,
      "type": "EXPENSE",
      "category": "Food",
      "date": "2026-04-02",
      "description": "Monthly grocery shopping"
    },
    {
      "id": 9,
      "amount": 5000.00,
      "type": "INCOME",
      "category": "Salary",
      "date": "2026-04-01",
      "description": "Monthly salary"
    }
  ]
}
```

**Notes**
- categoryTotals shows net amounts (income positive, expenses negative)
- recentActivity shows the 5 most recent records
- All non-deleted records are included in calculations

---

## Roles and Permissions

The application uses role-based access control with three roles:

| Role | Permissions |
|------|-------------|
| ADMIN | Full access: Create, read, update, delete financial records; Manage users |
| ANALYST | Read access: View financial records and summaries |
| VIEWER | Read access: View financial records and summaries |

### Endpoint Authorization Matrix

| Endpoint | ADMIN | ANALYST | VIEWER |
|----------|-------|---------|--------|
| POST /auth/register | ✓ | ✓ | ✓ |
| POST /auth/login | ✓ | ✓ | ✓ |
| POST /users | ✓ | ✗ | ✗ |
| GET /users | ✓ | ✗ | ✗ |
| GET /users/{id} | ✓ | ✗ | ✗ |
| GET /users/{username} | ✓ | ✗ | ✗ |
| PUT /users/{id}/activate | ✓ | ✗ | ✗ |
| PUT /users/{id}/deactivate | ✓ | ✗ | ✗ |
| PUT /users/{id}/roles | ✓ | ✗ | ✗ |
| POST /financial-records | ✓ | ✗ | ✗ |
| GET /financial-records | ✓ | ✓ | ✓ |
| PUT /financial-records/{id} | ✓ | ✗ | ✗ |
| DELETE /financial-records/{id} | ✓ | ✗ | ✗ |
| GET /financial-records/summary | ✓ | ✓ | ✓ |

---

## Error Responses

The API uses standard HTTP status codes and returns error responses in the following format:

```json
{
  "timestamp": "2026-04-04T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Financial record not found with id: 123",
  "path": "/api/financial-records/123"
}
```

### Common Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 OK | Request succeeded |
| 201 Created | Resource created successfully |
| 204 No Content | Request succeeded with no response body |
| 400 Bad Request | Invalid request data or validation error |
| 401 Unauthorized | Missing or invalid authentication token |
| 403 Forbidden | Insufficient permissions or inactive account |
| 404 Not Found | Resource not found |
| 409 Conflict | Resource already exists (e.g., duplicate username) |
| 500 Internal Server Error | Server error |

---

## Security

* JWT-based authentication with token expiration
* Role-based authorization (ADMIN / ANALYST / VIEWER)
* Stateless session management
* Password encryption using BCrypt
* Soft delete for data integrity
* Input validation on all endpoints

---

## Default Admin Credentials

```
Username: admin
Password: admin123
```

---

## Deployment Notes

* Configure environment variables for database credentials
* Use `prod` profile for production deployments
* Ensure secure storage of JWT secret and sensitive configurations

---

## Key Features

* Secure authentication using JWT
* Role-based access control
* RESTful API design
* Clean layered architecture
* PostgreSQL integration with Spring Data JPA

---

## Testing

All APIs have been tested using Postman with:

* Successful authentication flow (register → login → token usage)
* Authorized and unauthorized access validation
* CRUD operations for financial records
* Role-based access verification

---

## Summary

This project demonstrates the implementation of a secure, production-ready backend system with authentication, authorization, and financial data management using modern Spring Boot practices.
