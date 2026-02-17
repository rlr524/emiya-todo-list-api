# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

- **Build:** `./gradlew build`
- **Run:** `./gradlew bootRun`
- **Test all:** `./gradlew test`
- **Single test class:** `./gradlew test --tests "com.emiyaconsulting.emiya_todo_list_api.SomeTest"`
- **Single test method:** `./gradlew test --tests "com.emiyaconsulting.emiya_todo_list_api.SomeTest.methodName"`
- **Clean build:** `./gradlew clean build`

## Environment Variables

The app requires two env vars for MongoDB Atlas connectivity:
- `MONGO_USERNAME_TODO`
- `MONGO_PASSWORD_TODO`

## Architecture

Spring Boot 3.5.5 REST API (Java 21, Gradle Kotlin DSL) backed by MongoDB Atlas. All endpoints are prefixed with `/api/v1` via `server.servlet.context-path`.

### Layered Structure

```
com.emiyaconsulting.emiya_todo_list_api
├── controller/    — REST endpoints (@RestController, @Validated)
├── service/       — Business logic, uses MongoTemplate for complex queries
├── repository/    — Spring Data MongoDB repositories
├── model/         — @Document entities (User, Item)
├── exception/     — Custom RuntimeExceptions + @ControllerAdvice global handler
└── config/        — MongoDB configuration
```

### Domain Model

Two entities: **User** and **Item**. Items reference their owner by user ID (`owner` field).

### Key Patterns

- **Soft delete:** Both entities use `deleted` boolean + `deletedAt` timestamp instead of hard deletes. List endpoints filter out deleted records.
- **MongoDB auditing:** `@EnableMongoAuditing` on the main application class; entities use `@CreatedDate` / `@LastModifiedDate`.
- **Exception handling:** Domain-specific exceptions (e.g., `UserNotFoundException`, `ItemNotFoundException`) are caught by `ToDoListGlobalExceptionHandler` (@ControllerAdvice) and returned as 404 responses.
- **Constructor injection:** Services use constructor-based DI (no @Autowired).
- **Lombok:** Used for boilerplate reduction (@Data, @NonNull, @Builder, etc.).
