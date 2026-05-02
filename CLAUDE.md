# Spring Boot Microservice Architecture

## Project Overview

A Spring Boot microservice platform consisting of 3 independent services orchestrated with Docker Compose:

1. **User Service** - Manages user accounts and authentication
2. **Order Service** - Handles order processing and management
3. **Product Service** - Manages product catalog and inventory

## Architecture

- Each service is a standalone Spring Boot application
- Services communicate via REST APIs
- Database per service pattern (each has its own PostgreSQL database)
- Docker Compose manages service orchestration and networking
- All services are containerized and communicate over internal Docker network

## Directory Structure

```
microservice/
├── CLAUDE.md
├── docker-compose.yml
├── user-service/
│   ├── src/
│   │   ├── main/java/com/microservice/user/
│   │   │   ├── UserServiceApplication.java
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   └── repository/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-docker.yml
│   ├── Dockerfile
│   └── pom.xml
├── order-service/
│   ├── src/main/java/com/microservice/order/
│   ├── Dockerfile
│   ├── application.yml
│   └── pom.xml
└── product-service/
    ├── src/main/java/com/microservice/product/
    ├── Dockerfile
    ├── application.yml
    └── pom.xml
```

## Building & Running

### Prerequisites
- Docker and Docker Compose
- Java 17+ (for local development)
- Maven 3.8+

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### View Logs
```bash
docker-compose logs -f [service-name]
```

## Service Endpoints

- **User Service**: `http://localhost:8081`
- **Order Service**: `http://localhost:8082`
- **Product Service**: `http://localhost:8083`

## Environment

- **OS**: Windows 11
- **Primary Language**: Java (Spring Boot)
- **Container Runtime**: Docker
- **Database**: PostgreSQL
- **Build Tool**: Maven
