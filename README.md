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

## Authentication APIs

### Register User

**POST** `/api/auth/register`

```json
{
  "username": "testuser",
  "email": "testuser@example.com",
  "password": "password123",
  "roles": ["ANALYST"]
}
```

---

### Login

**POST** `/api/auth/login`

```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response**

```json
{
  "token": "JWT_TOKEN"
}
```

Use the token in subsequent requests:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## User APIs

### Get All Users (Admin Only)

**GET** `/api/users`

**Response**

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
    "status": "INACTIVE",
    "roles": []
  }
]
```

---

## Financial Record APIs

### Create Financial Record

**POST** `/api/financial-records`

```json
{
  "amount": 1200,
  "description": "Groceries",
  "category": "Food",
  "type": "EXPENSE",
  "date": "2026-04-02"
}
```

**Response**

```json
{
  "id": 4,
  "amount": 1200,
  "type": "EXPENSE",
  "category": "Food",
  "date": "2026-04-02",
  "description": "Groceries"
}
```

---

### Get All Financial Records

**GET** `/api/financial-records`

```json
[
  {
    "id": 4,
    "amount": 1200,
    "type": "EXPENSE",
    "category": "Food",
    "date": "2026-04-02",
    "description": "Groceries"
  }
]
```

---

### Financial Summary

**GET** `/api/financial-records/summary`

```json
{
  "totalRevenue": 500000,
  "totalExpense": 320000,
  "netProfit": 180000
}
```

---

## Security

* JWT-based authentication
* Role-based authorization (ADMIN / ANALYST / VIEWER)
* Stateless session management

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
