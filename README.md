# Finance Backend - Spring Boot Project

## Project Overview
This is a Spring Boot application for finance data management built with Maven and Java 17.

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
├── src/
│   ├── main/
│   │   ├── java/com/finance/backend/
│   │   │   └── FinanceBackendApplication.java   # Main application class
│   │   └── resources/
│   │       └── application.properties            # Application configuration
│   └── test/
│       └── java/com/finance/backend/
│           └── FinanceBackendApplicationTests.java
└── README.md                                  # This file
```

### Getting Started

#### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

#### Installation

1. **Clone/Navigate to project**:
   ```bash
   cd d:\JavaC\finance-data-backend
   ```

2. **Configure Database**:
   - Update `src/main/resources/application.properties` with your PostgreSQL connection details
   - Default configuration expects:
     - Host: localhost
     - Port: 5432
     - Database: finance_db
     - Username: postgres
     - Password: postgres

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

   The application will start at: `http://localhost:8080`

### Configuration

Key application properties in `application.properties`:
- `server.port`: Application server port (default: 8080)
- `server.servlet.context-path`: API context path (default: /api)
- Database connection settings
- JPA/Hibernate configuration
- Logging levels

### Development Notes

- **Lombok**: Configured for code generation (getters, setters, constructors, etc.)
- **Spring Security**: Included for authentication and authorization
- **Validation**: Use Jakarta Validation annotations for input validation
- **JPA**: Use Spring Data JPA for database operations

### Next Steps

After project setup, you can:
1. Create entity classes in `src/main/java/com/finance/backend/entity/`
2. Create repository interfaces in `src/main/java/com/finance/backend/repository/`
3. Create service classes in `src/main/java/com/finance/backend/service/`
4. Create REST controllers in `src/main/java/com/finance/backend/controller/`
5. Add custom security configuration in `src/main/java/com/finance/backend/config/`

### Troubleshooting

- **Maven issues**: Run `mvn clean install -U` to update dependencies
- **Lombok not working**: Ensure IDE has Lombok annotation processor enabled
- **Database connection errors**: Verify PostgreSQL is running and credentials are correct
- **Build failure**: Check Java version is 17+: `java -version`
