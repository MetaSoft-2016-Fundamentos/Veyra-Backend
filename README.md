# 🏥 Veyra — Backend for Nursing Homes

Veyra is the backend application designed to manage day-to-day operations of nursing homes and assisted living centers: residents, staff, activities, health tracking, authentication and APIs for web or mobile clients.

This repository follows Domain-Driven Design (DDD) principles and is organized into bounded contexts to keep responsibilities separated and allow the system to grow in a modular way.

Quick summary:
- Language: Java 25
- Framework: Spring Boot
- Persistence: MySQL (JPA/Hibernate)
- Migrations: Flyway
- API Documentation: Swagger / OpenAPI (springdoc-openapi)
- Build: Maven (includes `mvnw` / `mvnw.cmd`)

---

## Bounded contexts
The code is split by domain-relevant bounded contexts. Each represents a clear responsibility in the system:

- `activities` — Management of activities, schedules, resources and attendance tracking.
- `analytics` — Aggregation, metrics and report generation.
- `hcm` — Human capital management: employees, roles, payroll and hiring.
- `health` — Medical records, vital signs, medical events and alerts.
- `iam` — Identity and Access Management: JWT authentication, users, roles and permissions.
- `nursing` — Nursing-specific logic: care plans, tasks and shifts.
- `payments` — Billing, invoicing and payments processing.
- `profiles` — Resident profiles and associated metadata.
- `shared` — Shared components and utilities (common DTOs, exception handling, helpers).
- `tracking` — Location tracking, activity logs and auditing.

These contexts map to packages under `src/main/java/com/novaperutech/veyra/platform`.

---

## Project structure (summary)

- `src/main/java/.../activities` — Activities bounded context (application, domain, infrastructure, interfaces)
- `src/main/resources` — Spring configuration, message bundles and static resources
- `src/test/java` — Unit and integration tests
- `pom.xml` — Maven dependencies and build plugins

---

## Prerequisites

- JDK 25 installed and `JAVA_HOME` configured
- Maven (or use the included wrapper `mvnw` / `mvnw.cmd`)
- MySQL 8.x (or compatible)

---

## Configuration and environment variables

Spring properties live in `src/main/resources/application*.properties`. Key properties to configure for local or production runs:

- `spring.datasource.url` — JDBC URL for MySQL
- `spring.datasource.username` — Database user
- `spring.datasource.password` — Database password
- `spring.profiles.active` — Active Spring profile (e.g. `dev` or `prod`)
- `server.port` — Optional: server port

Security and JWT settings are usually defined in the `iam` module-specific properties. Use profiles (`application-dev.properties`, `application-prod.properties`) and environment variables for sensitive values in CI/CD.

---

## Database migrations

Flyway is used to manage database migrations. Place SQL migration scripts in `src/main/resources/db/migration` using the naming convention: `V{version}__{description}.sql`. Flyway will apply pending migrations automatically at application startup.

---

## Run locally

From PowerShell in the repository root you can run:

```powershell
# Run with the included Maven wrapper (Windows)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Or build and run the packaged jar
.\mvnw.cmd clean package -DskipTests
java -jar target\*.jar --spring.profiles.active=dev
```

Make sure your database is reachable and credentials are configured before starting the application.

---

## Tests

Run tests with Maven:

```powershell
.\mvnw.cmd test
```

Tests include unit and integration tests located under `src/test/java`.

---

## API documentation (Swagger / OpenAPI)

If `springdoc-openapi` is enabled, the OpenAPI JSON and Swagger UI will be available at runtime using standard endpoints:

- OpenAPI JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html` or `/swagger-ui/index.html`

Open `http://localhost:{server.port}/swagger-ui.html` in your browser to explore the API.

---

## Docker (optional)

You can containerize the application with a `Dockerfile` and use `docker-compose` to run the app together with a MySQL service during development. Example Dockerfile:

```dockerfile
# Example: build a small runnable image
FROM eclipse-temurin:25-jdk-jammy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

A `docker-compose.yml` can combine the application and MySQL with volumes for data persistence and an `.env` file for development credentials.

---

## Security and authentication

The `iam` module handles authentication (JWT) and role-based authorization. Review security configuration classes and property values to tune token expiration, secret keys and access policies.

---

## Best practices and recommendations

- Keep responsibilities separated by bounded context.
- Keep DTOs and public contracts stable for API consumers.
- Version database migrations and avoid editing migrations that are already applied in production.
- Add integration tests for critical flows (authentication, DB operations).

---

## Contributing

1. Create a branch with the `feature/` or `fix/` prefix.
2. Open a pull request with a clear description and reproduction steps.
3. Add tests for new logic and document API changes.

---

## Additional resources

- Architecture diagram: `docs/diagrams/veyra/veyra-backend-diagram.puml`
- Domain diagrams: `docs/diagrams/*`

---

## License

Add the project license here (for example: MIT, Apache-2.0) or include a `LICENSE` file at the repository root.

---

If you want, I can also:
- Add a concrete `application-dev.properties` example suitable for local development.
- Create a `docker-compose.yml` example for development using MySQL and an `.env.example` file.
- Generate a short production checklist (monitoring, backups, CI/CD steps).

Tell me which of these extras you prefer and I will create it.
