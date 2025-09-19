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
./mvnw clean compile     # Compile the project
./mvnw test             # Run tests
./mvnw package          # Create JAR
./mvnw spring-boot:run  # Run the application

# Development
./mvnw clean install    # Clean and install dependencies
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

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: PostgreSQL with Flyway migrations
- **ORM**: MyBatis 3.0.5 with MyBatis Dynamic SQL
- **Session Management**: Redis with Spring Session
- **Documentation**: SpringDoc OpenAPI 2.8.13
- **Build Tool**: Maven

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

The backend requires these environment variables:

- `PG_JDBC_URL`: PostgreSQL JDBC URL
- `PG_USER`: Database username
- `PG_DB`: Database name
- `PG_PASS`: Database password

### Database Development

- Database migrations are handled by Flyway
- Schema is defined in `docs/database.md` with ER diagram
- SQL formatting should use SQLFluff

## Frontend Development Requirements

### **Better TDD for Frontend**

- **Philosophy**: Test behavior over implementation, user experience over styling details
- **Process**: Write failing test → Minimal implementation → Small refactoring
- **All components must have corresponding test files**
- Test file naming: `ComponentName.spec.ts`
- Coverage must include main functionality and edge cases
- All tests must pass before committing

#### **Unit Testing Focus (Component Logic)**

- **Test what users see**: Text content, interactive elements, structure
- **Test component behavior**: Click handlers, state changes, user interactions
- **Use semantic selectors**: `aria-label`, `data-testid`, roles over CSS classes
- **Avoid implementation details**: Never test CSS class names, internal state, or styling

#### **Component Testability Requirements**

- **Use `data-testid`** for stable element identification
- **Add `aria-label`** to interactive elements for accessibility and testability
- **Structure over styling**: Test DOM structure, not visual presentation
- **Test functionality**: What the component does, not how it looks

### **STRICT** Coding Standards

#### **Component Format**

- Use `<script setup lang="ts">` syntax and `<script lang="ts"></script>` should be always on top of `<tmplates>`
- PascalCase component naming convention
- Strict TypeScript type checking enabled
- No bypassing type safety

#### **Design System Requirements (NON-NEGOTIABLE)**

- **DaisyUI Components**: Must use DaisyUI component classes
  - Card: `card`, `card-body`, `card-title`, `card-actions`
  - Button: `btn`, `btn-primary`, `btn-secondary`, `btn-lg`
  - Grid: `grid`, `md:grid-cols-2`, `lg:grid-cols-4`

#### **Color System (CRITICAL)**

- **Semantic Colors Only**: Hard-coded colors are **STRICTLY FORBIDDEN**
- **Forbidden**: `text-gray-500`, `text-gray-600`, `text-gray-800`, `bg-gray-100`, etc.
- **Required Semantic Classes**:
  - Text: `text-primary`, `text-secondary`, `text-accent`, `text-base-content/[opacity]`
  - Background: `bg-primary`, `bg-secondary`, `bg-accent`, `bg-base-100`, `bg-base-200`
  - Opacity: Use `/50`, `/60`, `/70`, `/80` variants (e.g., `text-base-content/60`)
  - Gradients: Use semantic transparency variants (e.g., `from-primary/5 to-secondary/10`)

#### **Icon Standards**

- **Icon Library**: Use unplugin-icons and Tabler Icons **FIRST**, For example:
  - **Icon Format**: `i-tabler-[icon-name]`
  - **Common Icons**:
    - Message: `i-tabler-message-circle`
    - Users: `i-tabler-users`
    - Login: `i-tabler-login`
    - Register: `i-tabler-user-plus`
    - Upload: `i-tabler-upload`

### **Development Workflow Requirements**

- **Better TDD is Non-Negotiable**: All development must follow behavior-first testing approach
- **Continuous Testing**: Run tests after every change
- **Code Quality**: ESLint, Prettier, and regular linting required
- **Clear commit messages** with all tests passing

### **Accessibility & Security**

- Semantic HTML5 tags
- Proper ARIA labels (enhances both accessibility and testability)
- Keyboard navigation support
- Color contrast compliance
- Dark mode support
- Frontend form validation
- XSS prevention
- No sensitive data in frontend storage

## Code Quality Standards

### Frontend

- Use ESLint with Vue TypeScript configuration
- Format with Prettier and Tailwind CSS plugin
- Type checking with vue-tsc
- Follow Vue 3 Composition API patterns
- Use Pinia for state management

### Backend

- Use Lombok for boilerplate reduction
- Follow Spring Boot best practices
- Use MyBatis Dynamic SQL for database operations
- Document APIs with SpringDoc OpenAPI
- Follow Java 21 features and conventions

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

#### **Testing Best Practices**

1. **Test User Experience**: What the user sees and does
2. **Use Stable Selectors**: `data-testid` and `aria-label` over CSS classes
3. **Test Functionality**: Component behavior over implementation details
4. **Accessibility First**: Proper ARIA labels improve both a11y and testability
5. **Avoid Brittleness**: Tests shouldn't break when styling changes

### Backend Testing

- **Unit Tests**: JUnit 5 with Spring Boot Test
- **Integration Tests**: Spring Boot Test with Testcontainers
- **Database Tests**: @DataJpaTest with H2 in-memory database

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
│   └── pom.xml        # Maven dependencies and configuration
├── src/               # Shared resources (mainly tests)
├── docs/              # Project documentation
│   ├── database.md    # Database schema and ER diagram
│   └── technical-selection-report.md # Technology choices
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
