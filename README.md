# Finance Backend - Spring Boot Project

## Project Overview
Finance Backend is a Spring Boot application for finance data management built with Maven and Java 17. It delivers a secure, JWT-based authentication flow over RESTful APIs so that recruiters and finance operators can consume consistent endpoints without dealing directly with authentication internals. The codebase follows a layered architecture (Controller → Service → Repository) that keeps controllers focused on HTTP concerns, services focused on business rules, and repositories focused on persistence.

### Technology Stack
- **Java Version**: 17
- **Spring Boot**: 3.2.4
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Dependencies**:
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - PostgreSQL JDBC Driver
  - Lombok

### Project Structure
```
finance-backend/
├── pom.xml                                    # Maven configuration
├── Dockerfile
├── README.md                                  # This file
└── src/
    ├── main/
    │   ├── java/com/finance/backend/
    │   │   ├── FinanceBackendApplication.java   # Main application class
    │   │   ├── config/                           # Spring config classes
    │   │   ├── controller/                       # REST controllers
    │   │   ├── dto/                              # Data transfer objects
    │   │   ├── entity/                           # JPA entities
    │   │   ├── exception/                        # Exception handlers
    │   │   ├── repository/                       # Spring Data repositories
    │   │   ├── security/                         # Security classes (JWT, config)
    │   │   └── service/                          # Business services
    │   └── resources/
    │       ├── application.properties
    │       └── application-prod.properties
    └── test/
        └── java/com/finance/backend/
            └── FinanceBackendApplicationTests.java
```

### Getting Started

#### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

#### Installation

1. **Clone/Navigate to the project**
   ```bash
   cd d:\JavaC\finance-data-backend
   ```

2. **Configure the database**
   - Provide database credentials via environment variables instead of checking secrets into source control:
     ```bash
     export DB_URL=<DB_URL>
     export DB_USERNAME=<DB_USERNAME>
     export DB_PASSWORD=<DB_PASSWORD>
     export SPRING_PROFILES_ACTIVE=dev
     ```
   - On Windows PowerShell, set variables like:
     ```powershell
     $env:DB_URL='<DB_URL>'
     $env:DB_USERNAME='<DB_USERNAME>'
     $env:DB_PASSWORD='<DB_PASSWORD>'
     $env:SPRING_PROFILES_ACTIVE='dev'
     ```
   - These variables map to the datasource configuration found in `src/main/resources/application.properties` and `application-prod.properties`.

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The application starts at: `http://localhost:8080`

### Configuration
- `server.port`: application port (default: 8080)
- `server.servlet.context-path`: API context path (default: `/api`)
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`: supplied through environment variables to avoid committed secrets
- `SPRING_PROFILES_ACTIVE`: selects between `dev` and `prod` settings
- JPA/Hibernate configuration and logging levels are scoped per profile in the properties files

### Development Notes
- **Lombok**: removes boilerplate for DTOs and entities via annotations
- **Spring Security**: enforces JWT-based authentication and role-based authorization across the REST endpoints
- **Validation**: Jakarta Validation annotations guard DTO inputs prior to executing business logic
- **JPA**: Spring Data JPA repositories manage persistence with PostgreSQL
- **Layered Architecture**: Controllers expose RESTful APIs, services orchestrate rules, and repositories handle database interactions for separation of concerns

### Next Steps
After project setup, you can:
1. Create entity classes in `src/main/java/com/finance/backend/entity/`
2. Create repository interfaces in `src/main/java/com/finance/backend/repository/`
3. Create service classes in `src/main/java/com/finance/backend/service/`
4. Create REST controllers in `src/main/java/com/finance/backend/controller/`
5. Add custom security configuration in `src/main/java/com/finance/backend/config/`

### Troubleshooting
- **Maven issues**: run `mvn clean install -U` to refresh dependencies
- **Lombok not working**: enable Lombok annotation processing in the IDE
- **Database connection errors**: ensure PostgreSQL is running and `DB_*` environment variables are populated
- **Build failure**: verify Java 17+ is active (`java -version`)

### API Testing (Postman)

#### Authentication Flow
##### `POST /api/auth/register`
- **Description**: register a new user with desired roles
- **Headers**:
  - `Content-Type: application/json`
- **Sample request JSON**:
  ```json
  {
    "username": "recruiter",
    "password": "StrongP@ssw0rd",
    "roles": ["USER"]
  }
  ```
- **Example response**:
  ```json
  {
    "message": "User registered successfully",
    "username": "recruiter",
    "roles": ["USER"]
  }
  ```

##### `POST /api/auth/login`
- **Description**: exchange credentials for a JWT token
- **Headers**:
  - `Content-Type: application/json`
- **Sample request JSON**:
  ```json
  {
    "username": "recruiter",
    "password": "StrongP@ssw0rd"
  }
  ```
- **Example response**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1...",
    "expiresIn": 3600
  }
  ```

After login, copy the `token` value and include it in the Authorization header for downstream requests:
```
Authorization: Bearer <JWT_TOKEN>
```

#### User APIs
##### `GET /api/users`
- **Description**: retrieve all user profiles (requires ADMIN role)
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
- **Sample request JSON**: _not applicable (GET)_
- **Example response**:
  ```json
  [
    {
      "id": 1,
      "username": "admin",
      "roles": ["ADMIN"],
      "status": "ACTIVE"
    }
  ]
  ```

##### `GET /api/users/{username}`
- **Description**: fetch details for a specific user
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
- **Sample request JSON**: _not applicable (GET)_
- **Example response**:
  ```json
  {
    "id": 2,
    "username": "recruiter",
    "roles": ["USER"],
    "status": "ACTIVE"
  }
  ```

##### `PUT /api/users/{id}/activate`
- **Description**: activate a disabled user
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
  - `Content-Type: application/json`
- **Sample request JSON**:
  ```json
  {
    "note": "Enable recruiting access"
  }
  ```
- **Example response**:
  ```json
  {
    "id": 2,
    "username": "recruiter",
    "status": "ACTIVE"
  }
  ```

##### `PUT /api/users/{id}/deactivate`
- **Description**: deactivate a user to revoke access
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
  - `Content-Type: application/json`
- **Sample request JSON**:
  ```json
  {
    "note": "Suspend due to policy"
  }
  ```
- **Example response**:
  ```json
  {
    "id": 2,
    "username": "recruiter",
    "status": "INACTIVE"
  }
  ```

#### Financial Record APIs
##### `POST /api/financial-records`
- **Description**: create a new finance record
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
  - `Content-Type: application/json`
- **Sample request JSON**:
  ```json
  {
    "type": "REVENUE",
    "amount": 12500,
    "currency": "USD",
    "description": "Q1 SaaS subscription",
    "date": "2026-03-31"
  }
  ```
- **Example response**:
  ```json
  {
    "id": 15,
    "type": "REVENUE",
    "amount": 12500,
    "currency": "USD",
    "description": "Q1 SaaS subscription",
    "date": "2026-03-31",
    "createdBy": "recruiter"
  }
  ```

##### `GET /api/financial-records`
- **Description**: list all finance records (supports pagination)
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
- **Sample request JSON**: _not applicable (GET)_
- **Example response**:
  ```json
  [
    {
      "id": 15,
      "type": "REVENUE",
      "amount": 12500,
      "currency": "USD",
      "description": "Q1 SaaS subscription",
      "date": "2026-03-31"
    }
  ]
  ```

##### `GET /api/financial-records/summary`
- **Description**: obtain aggregate metrics for finance dashboards
- **Headers**:
  - `Authorization: Bearer <JWT_TOKEN>`
- **Sample request JSON**: _not applicable (GET)_
- **Example response**:
  ```json
  {
    "totalRevenue": 500000,
    "totalExpense": 320000,
    "netProfit": 180000
  }
  ```

### Security Testing
- **Without JWT**: requests such as `GET /api/users` respond with `401 Unauthorized`; omit the `Authorization` header in Postman and confirm the API rejects the call.
- **With JWT**: include `Authorization: Bearer <JWT_TOKEN>` and verify the expected payload is returned, confirming Spring Security validates and parses the token before controller logic executes.

### Default Admin Credentials
- **Username**: `admin`
- **Password**: `admin123`
- These credentials are for development/testing only and must be changed or disabled before deploying to production.

### Production Deployment (Render + Neon Postgres)
1. **Render setup**
   - Link the repo `https://github.com/akashgupta2233/finance-data-backend.git` and select the desired branch.
   - Set the build command to `./mvnw clean package` and the start command to `java -jar target/finance-backend-0.0.1-SNAPSHOT.jar` (update the jar name after version changes).
2. **Environment variables**
   - `DB_URL=<DB_URL>`
   - `DB_USERNAME=<DB_USERNAME>`
   - `DB_PASSWORD=<DB_PASSWORD>`
   - `SPRING_PROFILES_ACTIVE=prod`
   - Ensure the service binds to the `PORT` environment variable provided by Render.
3. **Verification**
   - Review Render logs to confirm the `prod` profile loads and that the application connects to Neon Postgres via the supplied environment variables.
   - Request a JWT from the authentication endpoints and call User/FinancialRecord APIs to ensure role-based access control behaves as expected.

### 7. Documentation
- README clarity: outlines JWT-secured REST endpoints, environment variable configuration, and the layered architecture so reviewers can onboard quickly.
- Setup process: prerequisites, environment variable guidance, build, and run commands provide a repeatable workflow for recruiters or reviewers.
- API explanation: the new Postman section documents authentication, user, and financial endpoint payloads along with testing notes.
- Assumptions made: PostgreSQL is the persistence store, credentials are supplied via environment variables, JWT-based authentication secures the REST endpoints, and a role-permission model governs access.
- Tradeoffs considered: the repository favors a monolithic layered service for maintainability, avoids Swagger to keep dependencies minimal, and enforces secrets via environment variables to stay production-safe.
