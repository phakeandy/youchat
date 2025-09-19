# Backend Testing Configuration

This document describes the testing setup for the YouChat backend application.

## Test Categories

### 1. Unit Tests
- **Purpose**: Test individual components in isolation
- **Profile**: `test`
- **Database**: H2 in-memory database
- **Docker Compose**: Disabled
- **Location**: `src/test/java/**/*Test.java`
- **Command**: `mvn test`

### 2. Slice Tests
- **Purpose**: Test specific layers (web, data, etc.)
- **Profile**: `test-slice`
- **Annotations**: `@WebMvcTest`, `@DataJpaTest`, `@WebFluxTest`
- **Docker Compose**: Disabled
- **Command**: `mvn test`

### 3. Integration Tests
- **Purpose**: Test full application with real containers
- **Profile**: `integration-test`
- **Database**: PostgreSQL container via Testcontainers
- **Redis**: Redis container via Testcontainers
- **Docker Compose**: Disabled (using Testcontainers instead)
- **Command**: `mvn test -Dtest=*IntegrationTest`

## Running Tests

### All Tests
```bash
mvn test
```

### Unit Tests Only
```bash
mvn test -Dtest=*Test -DexcludeTests=*IntegrationTest
```

### Integration Tests Only
```bash
mvn test -Dtest=*IntegrationTest
```

### Specific Test Class
```bash
mvn test -Dtest=UserControllerTest
```

## Test Configuration

### Profiles
- `test`: Unit tests with H2 database
- `test-slice`: Slice tests with minimal context
- `integration-test`: Full integration tests with Testcontainers

### Database Configuration
- **Unit Tests**: H2 in-memory database
- **Integration Tests**: PostgreSQL container
- **Migration**: Flyway enabled for all test types

### Docker Compose
Docker Compose is **disabled** for all tests. Instead, we use:
- H2 database for unit tests
- Testcontainers for integration tests

### Quiet Logging
Tests use minimal logging output:
- Root level: ERROR
- Application packages: DEBUG
- Spring Boot: ERROR

## Test Dependencies

### Required Dependencies
- `spring-boot-starter-test`
- `mybatis-spring-boot-starter-test`
- `h2` (for unit tests)
- `spring-boot-testcontainers`
- `testcontainers` modules

### Example Test Structure

```java
// Unit Test
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    // H2 database, quiet logging
}

// Slice Test
@WebMvcTest(UserController.class)
@ActiveProfiles("test-slice")
public class UserControllerTest {
    // Mocked dependencies, minimal context
}

// Integration Test
@SpringBootTest
@Testcontainers
@ActiveProfiles("integration-test")
public class UserIntegrationTest {
    // Real PostgreSQL and Redis containers
}
```

## Test Database Schema

Flyway migrations are applied automatically for integration tests. The schema matches the production database structure.

## Troubleshooting

### Common Issues
1. **Docker not running**: Required for integration tests
2. **Port conflicts**: Testcontainers uses random ports
3. **Memory issues**: Increase JVM memory for integration tests

### Debugging
Enable debug logging by setting:
```bash
mvn test -Dlogging.level.top.phakeandy.youchat=DEBUG
```