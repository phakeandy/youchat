# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

YouChat is a modern full-stack chat application with Vue 3 frontend and Spring Boot backend, designed as a simplified WeChat clone. The project supports user management, friend management, one-on-one messaging, group chats, and file transfers.

## Architecture

- **Frontend**: Vue 3 + TypeScript + Tailwind CSS + DaisyUI (in `frontend/` directory)
- **Backend**: Spring Boot 3.5.5 + Java 21 + PostgreSQL + Redis (in `backend/` directory)
- **Database**: PostgreSQL 15+ with comprehensive chat schema and Redis for session management
- **Testing**: Vitest (unit tests) + Playwright (E2E tests) for frontend, standard Spring Boot testing for backend

## Development Commands

### Frontend (Vue 3)

```bash
cd frontend

# Development
pnpm dev              # Start development server
pnpm build            # Build for production
pnpm type-check       # Run TypeScript type checking
pnpm preview          # Preview production build

# Testing
pnpm test             # Run unit tests (Vitest)
pnpm test:run         # Run tests once
pnpm test:coverage    # Run tests with coverage
pnpm test:ui          # Run tests with UI
pnpm test:e2e         # Run E2E tests (Playwright)
pnpm test:e2e:ui      # Run E2E tests with UI
pnpm test:e2e:headed  # Run E2E tests in headed mode

# Code Quality
pnpm lint             # Run ESLint with auto-fix
pnpm format           # Format code with Prettier
```

### Backend (Spring Boot)

```bash
cd backend

# Standard Maven commands
# 你可以使用 `mvnd` 的 `-q` 选项来运行 maven 命令，获得更加纯净的输出
mvnd clean compile     # Compile the project
mvnd test             # Run tests
mvnd package          # Create JAR
mvnd spring-boot:run  # Run the application

# Development
mvnd clean install    # Clean and install dependencies
source .envrc && mvnd mybatis-generator:generate # Generate MyBatis 需要的样板代码，注意在你运行 clean 之后需要首先运行这个命令
docker compose up -d    # 项目配置了 spring docker compose support，但有时候可能还是需要手动运行 docker

# Fomatting and Linting
mvnd spotless:apply  # Format code with Google Java Format, 可以去除 Unused imports，和 import order
sqlfluff format --dialect postgres src/main/resources/db/migration
sqlfluff lint --dialect postgres src/main/resources/db/migration

# Code Quality Tools
mvnd checkstyle:check # Run Checkstyle validation
mvnd pmd:check        # Run PMD static analysis
mvnd spotbugs:check   # Run SpotBugs security analysis
mvnd verify           # Run all quality checks and tests
```

## Key Technologies & Dependencies

### Frontend Stack

- **Framework**: Vue 3.5.18 with Composition API
- **Build Tool**: Vite 7.0.6
- **Styling**: Tailwind CSS 4.1.13 + DaisyUI 5.1.12
- **State Management**: Pinia 3.0.3
- **Routing**: Vue Router 4.5.1
- **Testing**: Vitest 3.2.4 + Vue Test Utils 2.4.6 + Playwright 1.55.0
- **Package Manager**: pnpm (Node ^20.19.0 || >=22.12.0)

### Backend Stack

- **Framework**: Spring Boot 3.5.5 with Spring Security and Spring Session
- **Language**: Java 21
- **Architecture**: RESTful API with前后端分离
- **Database**: PostgreSQL with Flyway migrations
- **ORM**: MyBatis 3.0.5 with MyBatis Dynamic SQL (NOT JPA)
- **Session Management**: Redis with Spring Session
- **Documentation**: SpringDoc OpenAPI 2.8.13
- **Build Tool**: Maven
- **Code Quality**: Google Java Format, Checkstyle, PMD, SpotBugs, Spotless

## Database Architecture

The application uses a comprehensive PostgreSQL schema with these core entities:

- **users**: User accounts with authentication and settings
- **chat_groups**: Group chat management with ownership
- **group_members**: Group membership with roles
- **friendships**: User relationships with extra info
- **messages**: Chat messages with JSON content and conversation IDs
- **message_read_records**: Read receipts for messages

Key design decisions:

- JSONB columns for flexible content storage (messages.content, users.settings)
- Timestamps with timezone for all temporal data
- Composite keys for friendships table
- Environment-based database configuration

## Development Environment

### Prerequisites

- Node.js ^20.19.0 || >=22.12.0
- Java 21
- PostgreSQL 15+
- Redis
- pnpm package manager

### Environment Variables

需要的环境变量全部在 `.envrc` 文件中定义

### Database Development

- Database migrations are handled by Flyway
- Schema is defined in `docs/database.md` with ER diagram
- SQL formatting should use SQLFluff

## Backend Development Requirements

### Better TDD for Backend

- **Philosophy**: Test-driven development with failing tests first
- **Process**: Write failing test → Minimal implementation → Refactor
- All production code must have corresponding tests
- Test file naming: `ClassNameTest.java`
- Coverage must include business logic, edge cases, and API endpoints
- All tests and quality checks must pass before committing

#### Testing Strategy

- **Unit Tests**: JUnit 5 with Mockito for isolated component testing
- **Slice Tests**: Spring Boot test slices (`@WebMvcTest`, `@DataJpaTest`, `@WebFluxTest`)
- **Integration Tests**: `@SpringBootTest` with Testcontainers for full stack testing
- **API Tests**: RESTful endpoint testing with MockMvc and structured assertions

#### RESTful API Testing Requirements

- **Use JsonPath for JSON response validation** - NEVER test string equality
- **Test status codes, headers, and response structure
- **Use `@MockBean` for dependency isolation in slice tests
- **Test error scenarios and edge cases
- **Avoid testing implementation details, focus on API contracts

#### Database Testing

- **Unit Tests**: H2 in-memory database for fast testing
- **Integration Tests**: Testcontainers with PostgreSQL for realistic testing
- **Use `@Transactional` for test isolation
- **Test MyBatis mapper SQL queries and data access logic

### STRICT** Coding Standards

#### Code Quality Requirements

- **Checkstyle**: Google Java Style compliance
- **PMD**: Static code analysis for code quality and best practices
- **SpotBugs**: Bytecode analysis for bug detection
- **Spotless**: Code formatting and import ordering
- **All quality tools must pass** before 你标记提示我的这个需求完成了

#### Spring Security 8.x Best Practices

- **Use latest Spring Security 6.x APIs** (compatible with Spring Boot 3.5.5):
  - Use `UsernamePasswordAuthenticationToken.unauthenticated()` for creating unauthenticated tokens
  - Implement thread-safe context management with `SecurityContextHolderStrategy`
  - Manage session storage through `SecurityContextRepository`
  - Access authenticated user information with `@AuthenticationPrincipal` annotation

- **Authentication Flow Standardization**:
  - Controller layer: Receive requests, validate input, call authentication manager
  - Service layer: Implement `UserDetailsService` and `AuthenticationProvider`
  - Configuration layer: Use `HttpSecurity` for security configuration
  - Exception handling: Customize `AuthenticationEntryPoint` and `AccessDeniedHandler`

- **Session Management**:
  - Use Spring Session + Redis for distributed session management
  - Configure appropriate session timeout and concurrency control
  - Implement session fixation protection

#### Controller Layer Data Transfer Standards

“廋” Contrller “胖” Service 原则：不要编写复杂的 Controller，Controller 的指责应该尽量单一（接收HTTP请求，解析请求参数（路径变量、查询参数、请求体等）。），更多的业务逻辑应该在 Service 层处理。

- **DTO/VO Usage Requirement**: All controller input parameters and output responses must be encapsulated using DTO/VO objects
  - **Input DTOs**: Use `FooRequest.java` naming convention (e.g., `LoginRequest.java`, `CreateUserRequest.java`)
  - **Output VOs**: Use `FooResponse.java` naming convention (e.g., `LoginResponse.java`, `UserInfoResponse.java`)
  - **Strict Prohibition**: Never use raw `Map<String, Object>` for request/response data transfer

- **Use Java records for all DTOs and VOs (NON-NEGOTIABLE)**.
  - **Rationale**: record classes are the modern standard for immutable data carriers in Java. They enforce immutability, significantly reduce boilerplate code, and provide canonical equals(), hashCode(), and toString() implementations out of the box.
  - **Strict Prohibition**: The use of traditional Lombok-annotated classes (@Data, @Builder) for DTOs/VOs is now **DEPRECATED** in this project. Only use them if a record is fundamentally incompatible with a required library feature (a rare exception that must be justified).
  - All Request and Response objects must be defined as record.
  - **Use Validation Annotations**: Input DTOs (\*Request.java) must use Jakarta Validation annotations (@NotNull, @Size, @Email, etc.) for declarative validation.
  - **Serialization with Jackson**: Records work seamlessly with Jackson for JSON serialization/deserialization. Use @JsonProperty only when the desired JSON key name differs from the record component name.
  - **Keep DTOs/VOs Simple**: Records should only contain data and potentially compact constructors for validation or normalization. Business logic belongs in service layers.

- **API Response Standard**:
  - Semantic HTTP Status Codes (**NON-NEGOTIABLE**)
  - **Anti-Pattern Prohibition**: The use of a generic wrapper object (e.g., ApiResponse<T>, ResultDTO<T>) that always returns an HTTP 200 OK status, with the real status code hidden inside the JSON body, is STRICTLY PROHIBITED. 
  - No Manual Error Handling in Controllers: Controllers MUST NOT contain try-catch blocks for business logic exceptions or manually construct error responses (e.g., return ResponseEntity.badRequest(...)). This clutters the controller and leads to inconsistent error handling.
  - Throw Custom Exceptions: Business logic errors from the service layer MUST be signaled by throwing specific, descriptive custom exceptions (e.g., ResourceNotFoundException, DuplicateEmailException).
  - Global Exception Handler: All custom exceptions MUST be handled in a centralized `@RestControllerAdvice` component. This ensures separation of concerns and consistent error responses across the entire application. Standardized Error Format (**CRITICAL**): All error responses for client-facing errors (4xx and 5xx status codes) MUST conform to the RFC 7807 "Problem Details for HTTP APIs" standard. Spring Boot has native support for this, which must be enabled and utilized.
  - 返回给用户的错误消息，如果你认为会在前端展示，那么就填写中文，不要用英文。比如：
    - 错误实践：`@NotBlank(message = "Username is required") private String username;`
    - 正确实践：`@NotBlank(message = "用户名不能为空") private String username;`

#### MyBatis Best Practices

- **Code Generator Configuration**:
  - MyBatis Generator generates code to `target/generated-sources/mybatis-generator`
  - No need to manually create mapper folders in `src`
  - Use `build-helper-maven-plugin` to automatically add generated sources to compilation path

- **Dynamic SQL Usage**:
  - Prioritize MyBatis Dynamic SQL for type-safe query building
  - Avoid handwritten SQL strings, use `SqlBuilder` and `StatementBuilder`
  - Leverage `@SelectProvider` and `@UpdateProvider` annotations

#### Lombok Configuration

- **Lombok is configured**: No need to manually write getter, setter, toString methods
- **Common annotations**:
  - `@Data`: Auto-generate all common methods
  - `@RequiredArgsConstructor`: Generate final field constructor
  - `@Slf4j`: Auto-generate log field
  - `@Builder`: Support builder pattern

#### Testing Strategy Enhancement

- **Unit Tests**: Use Mockito for dependency isolation, JsonPath for JSON response validation
- **Integration Tests**: Use Testcontainers for real PostgreSQL database testing instead of H2 in-memory database
- **Test Data Management**: Use `@Transactional` to ensure test isolation with automatic rollback
- **API Testing**: Use MockMvc for RESTful endpoint testing, avoid string comparison

#### RESTful API Standards

- **Follow REST conventions**: Use proper HTTP methods and status codes
- **JSON responses**: Consistent response structure with proper content types
- **Error handling**: Standardized error response format
- **Documentation**: OpenAPI/Swagger documentation for all endpoints

##### OpenAPI Documentation

- 使用在后端的 Controller 和 Dto Vo 里面的生成 openapi 文档作为前后端交流的唯一凭证
- 不要在 `@ApiREsponses` 注解里面包含 `500` 错误码

#### Security Standards

- **Spring Security**: Proper authentication and authorization
- **Input validation**: Validate all user inputs
- **SQL injection prevention**: Use MyBatis parameterized queries
- **Sensitive data**: Never log sensitive information
- **CORS configuration**: Proper cross-origin resource sharing

### Backend Better TDD Development Workflow

**Better TDD Approach**: 记住拿到一个需求，先创建或者更新测试再更新实现

Better TDD 开发范式 + API First：

- **计划阶段** 首先，分析我的需求。然后，以列表形式打印出你详细的实现计划。等待我确认。
- **契约阶段** 计划确认后，首先在 Controller 类中创建空的API方法，并使用 OpenAPI 3 (Swagger) 注解（如 @Operation, @ApiResponses, @ApiResponse）为其编写完整的API文档。这必须定义出请求体、成功的响应体以及所有可预见的错误响应（例如400, 401, 404）。
- **测试阶段 - RED** 接下来，为这个API编写一个或多个单元测试（在...Test.java中）。这些测试应该基于上一步定义的API契约，并且现在必然会失败。
- **实现阶段 - GREEN** 现在，开始编写业务逻辑（通常在Service层和Repository层）以及Controller的实现代码。你的目标是让所有测试都通过。请遵循Spring Boot的最佳实践，例如分层架构、依赖注入等。
- **质量门禁** 在所有测试通过后，运行 Formatter。然后运行代码质量检查工具 `mvnd verify`（Checkstyle, PMD，spotbugs）。确保代码符合规范后，再向我报告任务完成。
  - 可以忽略 spotbugs 的低优先级警告

### Anti-Patterns to Avoid

- **Testing JSON string equality**: Use JsonPath assertions instead
- **Bypassing quality tools**: All tools must pass before committing
- **Skipping tests**: All tests must pass in clean environment
- **Hardcoding values**: Use constants and configuration properties
- **Ignoring security best practices**: Follow Spring Security guidelines

## Frontend Development Requirements

### Better TDD for Frontend

- **Philosophy**: Test behavior over implementation, user experience over styling details
- **Process**: Write failing test → Minimal implementation → Small refactoring
- All components must have corresponding test files
- Test file naming: `ComponentName.spec.ts`
- Coverage must include main functionality and edge cases
- All tests must pass before committing

#### Unit Testing Focus (Component Logic)

- **Test what users see**: Text content, interactive elements, structure
- **Test component behavior**: Click handlers, state changes, user interactions
- **Use semantic selectors**: `aria-label`, `data-testid`, roles over CSS classes
- **Avoid implementation details**: Never test CSS class names, internal state, or styling

#### Component Testability Requirements

- **Use `data-testid`** for stable element identification
- **Add `aria-label`** to interactive elements for accessibility and testability
- **Structure over styling**: Test DOM structure, not visual presentation
- **Test functionality**: What the component does, not how it looks

### STRICT** Coding Standards

#### Component Format

- Use `<script setup lang="ts">` syntax and `<script lang="ts"></script>` should be always on top of `<tmplates>`
- PascalCase component naming convention
- Strict TypeScript type checking enabled
- No bypassing type safety

#### Design System Requirements (NON-NEGOTIABLE)

- **DaisyUI Components**: Must use DaisyUI component classes
  - Card: `card`, `card-body`, `card-title`, `card-actions`
  - Button: `btn`, `btn-primary`, `btn-secondary`, `btn-lg`
  - Grid: `grid`, `md:grid-cols-2`, `lg:grid-cols-4`

#### Color System (CRITICAL)

- **Semantic Colors Only**: Hard-coded colors are **STRICTLY FORBIDDEN
- **Forbidden**: `text-gray-500`, `text-gray-600`, `text-gray-800`, `bg-gray-100`, etc.
- **Required Semantic Classes**:
  - Text: `text-primary`, `text-secondary`, `text-accent`, `text-base-content/[opacity]`
  - Background: `bg-primary`, `bg-secondary`, `bg-accent`, `bg-base-100`, `bg-base-200`
  - Opacity: Use `/50`, `/60`, `/70`, `/80` variants (e.g., `text-base-content/60`)
  - Gradients: Use semantic transparency variants (e.g., `from-primary/5 to-secondary/10`)

#### Icon Standards

- **Icon Library**: Use unplugin-icons and Tabler Icons **FIRST**, For example:
  - **Icon Format**: `i-tabler-[icon-name]` e.g. `i-tabler-message-circle`

### Development Workflow Requirements

- **Better TDD is Non-Negotiable**: All development must follow behavior-first testing approach
- **Continuous Testing**: Run tests after every change
- **Code Quality**: ESLint, Prettier, and regular linting required
- **Clear commit messages** with all tests passing

### Accessibility & Security

- Semantic HTML5 tags
- Proper ARIA labels (enhances both accessibility and testability)
- Keyboard navigation support
- Color contrast compliance
- Dark mode support
- Frontend form validation
- XSS prevention
- No sensitive data in frontend storage

## Testing Strategy

### Frontend Testing Philosophy

**Better TDD Approach**: Test behavior, not implementation. Focus on user experience rather than styling details.

- **Unit Tests (Component Logic)**: Vitest with Vue Test Utils
  - Focus: Component functionality, user interactions, state management
  - Avoid: CSS classes, styling details, internal implementation
  - Use: `aria-label`, `data-testid`, semantic selectors

- **Component Tests**: Structure and behavior verification
  - Test: What users see and interact with
  - Verify: Text content, interactive elements, DOM structure
  - Never: Test visual presentation or CSS properties

- **E2E Tests**: Playwright with multi-browser support
  - Focus: Complete user workflows, integration testing
  - Verify: Real user scenarios, cross-browser compatibility
  - Optional: Visual regression testing (when needed)

- **Coverage**: V8 coverage provider
- **Test Location**: Co-located with source files (`__tests__/` directories)

#### Testing Best Practices

1. **Test User Experience**: What the user sees and does
2. **Use Stable Selectors**: `data-testid` and `aria-label` over CSS classes
3. **Test Functionality**: Component behavior over implementation details
4. **Accessibility First**: Proper ARIA labels improve both a11y and testability
5. **Avoid Brittleness**: Tests shouldn't break when styling changes

### Backend Testing Strategy

不再做单元和切片测试，专注于集成测试。

#### 一、 集成测试 (@SpringBootTest + Testcontainers)

- 文件命名：一个 Controller 对应一个集成测试类，使用 @SpringBootTest 和 @Testcontainers，在测试类名后面加上Integration Test 例如，对于 AuthenticationController.java，它的集成测试类应该是 AuthenticationControllerIntegrationTest.java。
- 核心焦点： 测试从 API 端点到数据库的**完整后端应用调用链**。
- 测试环境： 使用 Testcontainers 启动**所有真实的外部依赖**，如 **PostgreSQL**、**Redis** 等，不要使用 h2 数据库，搭建一个准生产环境。
- 测试范围： 测试跨越多个组件（Controller -> Service -> Mapper -> DB）的**完整业务工作流**，验证组件间的协作是否正确。
- 测试数据： 通过数据构建者（Data Builders）、固件（Fixtures）或 SQL 脚本（@Sql）来管理测试数据。

#### 二、 API 测试最佳实践
        
- JSON 断言： 使用 JsonPath 进行健壮、精确的 JSON 响应体断言。
- HTTP 状态码： 全面测试各种在 API 契约中描述的状态码
- 响应头： 验证关键响应头，如 Content-Type，以及创建资源后返回的 Location。
- 场景覆盖： 测试所有关键场景，包括成功路径（Happy Path）、所有可预见的错误场景（如无效输入、资源不存在）和边界条件。
- 列表接口： 针对返回列表的接口，必须测试其分页和筛选功能。
    
#### 三、 避免反模式

1. **不测试框架：** 禁止测试 Spring 注解（如 `@Autowired`）、Getter/Setter、Mybatis 的基础 CRUD 或 Jackson 序列化。假设框架工作正常。
2. **不测试实现细节：** 只测试公共方法的行为（输入/输出、状态改变、对外交互），**绝不**通过反射测试私有方法。
3. **智能 Mock：**
    - Mock 外部依赖（如数据库、第三方客户端）。
    - **禁止**仅验证“方法是否被调用”（`verify(mock).someMethod()`）。必须验证**调用的参数**是否正确，或使用 Mock 来定义依赖的**行为**（如 `when(...).thenReturn(...)`）以驱动业务逻辑测试。
4. **聚焦业务逻辑：** 测试应覆盖核心的业务规则、条件分支、计算和异常流程。
5. 无需性能测试
6. 无需强调测试覆盖度，不要编写低质量的“凑数”测试。

## Project Structure

```text
youchat/
├── frontend/           # Vue 3 frontend application
│   ├── src/           # Source code
│   │   ├── components/ # Reusable Vue components
│   │   ├── views/      # Page components
│   │   ├── stores/     # Pinia state stores
│   │   └── __tests__/  # Test files
│   └── package.json    # Frontend dependencies and scripts
├── backend/            # Spring Boot backend application
│   ├── src/           # Java source code
│   │   ├── main/      # Main application code
│   │   └── test/      # Test code
├   ├── compose.yml    # Docker compose file for development
│   └── pom.xml        # Maven dependencies and configuration
├── src/               # Shared resources (mainly tests)
├── docs/              # Project documentation
│   ├── database.md    # Database schema and ER diagram
│   └── technical-selection-report.md # Technology choices
├── .envrc             # Environment variables，包含数据库连接信息等等
└── README.md          # Project overview
```

## Important Notes

- The project uses pnpm as the package manager for frontend
- Backend uses standard Maven build system
- Database schema is versioned and uses Flyway for migrations
- Redis is used for session management and caching
- The application is designed for scalability with PostgreSQL and Redis
- All database connections use environment variables for security
- Testing is comprehensive with both unit and E2E coverage
- 使用中文与我交流（代码注释没有特殊要求写英文）