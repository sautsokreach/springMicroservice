---
name: Spring Boot Microservice Project
description: 3-service Spring Boot architecture with Docker Compose orchestration
type: project
---

Spring Boot microservice project with 3 independent services orchestrated via Docker Compose:

- **User Service** (port 8081): Manages user accounts, authentication
- **Order Service** (port 8082): Order processing with cross-service calls to User and Product services
- **Product Service** (port 8083): Product catalog and inventory management

**Database Setup:**
- Separate PostgreSQL database per service (database per service pattern)
- User DB on port 5432, Order on 5433, Product on 5434
- Credentials: postgres:postgres (Docker only, change in production)

**Tech Stack:**
- Spring Boot 3.2.0, Java 17
- Maven for builds
- JPA/Hibernate for ORM
- Docker & Docker Compose for orchestration
- Lombok for boilerplate reduction

**Key Files:**
- `docker-compose.yml` - Full orchestration (3 services + 3 DBs)
- `CLAUDE.md` - Project documentation
- `README.md` - Complete API reference and setup guide
- Each service has own `pom.xml` and Dockerfile (multi-stage build)

**To Build & Run:**
```bash
docker-compose up -d
```

Services auto-configure through Spring profiles (docker profile for containerized deployment).
