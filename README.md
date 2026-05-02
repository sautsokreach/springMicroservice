# Spring Boot Microservice Architecture

A demonstration of a three-service microservice architecture built with Spring Boot and Docker Compose.

## Services

### 1. User Service (Port 8081)
Manages user accounts and authentication.

**Endpoints:**
- `POST /api/v1/users` - Create a new user
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/email/{email}` - Get user by email
- `GET /api/v1/users` - Get all users
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### 2. Order Service (Port 8082)
Handles order processing and management.

**Endpoints:**
- `POST /api/v1/orders` - Create a new order
- `GET /api/v1/orders/{id}` - Get order by ID
- `GET /api/v1/orders/user/{userId}` - Get orders by user
- `GET /api/v1/orders/product/{productId}` - Get orders by product
- `GET /api/v1/orders` - Get all orders
- `PUT /api/v1/orders/{id}` - Update order
- `DELETE /api/v1/orders/{id}` - Delete order

### 3. Product Service (Port 8083)
Manages product catalog and inventory.

**Endpoints:**
- `POST /api/v1/products` - Create a new product
- `GET /api/v1/products/{id}` - Get product by ID
- `GET /api/v1/products/sku/{sku}` - Get product by SKU
- `GET /api/v1/products/category/{category}` - Get products by category
- `GET /api/v1/products` - Get all products
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Delete product

## Prerequisites

- Docker and Docker Compose
- Java 17+ (optional, for local development)
- Maven 3.8+ (optional, for local builds)

## Quick Start

### Using Docker Compose

1. **Start all services:**
   ```bash
   docker-compose up -d
   ```

2. **Check service status:**
   ```bash
   docker-compose ps
   ```

3. **View logs:**
   ```bash
   docker-compose logs -f [service-name]
   ```

4. **Stop all services:**
   ```bash
   docker-compose down
   ```

### Local Development

1. **Build individual services:**
   ```bash
   cd user-service
   mvn clean package
   cd ../order-service
   mvn clean package
   cd ../product-service
   mvn clean package
   ```

2. **Run services locally:**
   Each service uses default ports:
   - User Service: 8080
   - Order Service: 8080 (configure different port in application.yml)
   - Product Service: 8080 (configure different port in application.yml)

## Database Setup

Services use PostgreSQL with the following databases:
- **user_service** (port 5432)
- **order_service** (port 5433)
- **product_service** (port 5434)

Default credentials: `postgres:postgres`

Databases are automatically created by Docker Compose.

## Example API Calls

### Create a User
```bash
curl -X POST http://localhost:8081/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "password": "password123"
  }'
```

### Create a Product
```bash
curl -X POST http://localhost:8083/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 50,
    "sku": "LAP-001",
    "category": "Electronics"
  }'
```

### Create an Order
```bash
curl -X POST http://localhost:8082/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 1999.98,
    "status": "PENDING"
  }'
```

## Architecture

```
┌─────────────────────────────────────────┐
│         Client Applications             │
└────────────────────────────────────────┬┘
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
        ▼                 ▼                 ▼
    ┌────────┐        ┌────────┐       ┌────────┐
    │ User   │        │ Order  │       │Product │
    │Service │        │Service │       │Service │
    └───┬────┘        └───┬────┘       └───┬────┘
        │                 │                 │
        ▼                 ▼                 ▼
    ┌────────┐        ┌────────┐       ┌────────┐
    │ User   │        │ Order  │       │Product │
    │  DB    │        │  DB    │       │  DB    │
    └────────┘        └────────┘       └────────┘
```

## Environment Variables

Services use the following environment variables (configured in docker-compose.yml):

- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SPRING_PROFILES_ACTIVE` - Active profile (docker for containerized deployment)
- `USER_SERVICE_URL` - URL to user service (for order service)
- `PRODUCT_SERVICE_URL` - URL to product service (for order service)

## Monitoring

### Check service health:
```bash
docker-compose logs user-service
docker-compose logs order-service
docker-compose logs product-service
```

### Database connectivity:
```bash
docker exec -it user-db psql -U postgres -d user_service -c "\dt"
```

## Troubleshooting

### Port conflicts
If ports are already in use, modify the port mappings in `docker-compose.yml`.

### Database connection errors
Ensure the database health checks pass:
```bash
docker-compose ps
```

### Service startup failures
Check logs for the specific service:
```bash
docker-compose logs [service-name]
```

## Development Notes

- Services use Spring Boot 3.2.0 with Java 17
- Database per service pattern for data isolation
- REST APIs for inter-service communication
- Auto-schema generation using Hibernate DDL
- Lombok for reducing boilerplate code

## Next Steps

1. Implement inter-service communication with RestTemplate or WebClient
2. Add API documentation with Springdoc OpenAPI
3. Implement distributed logging and tracing
4. Add circuit breakers with Resilience4j
5. Implement API gateway
6. Add message queuing (Kafka/RabbitMQ)
7. Implement service discovery
