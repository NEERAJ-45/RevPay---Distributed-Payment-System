# Getting Started - RevPay

## Prerequisites

- Docker and Docker Compose
- Java 21+
- Maven 3.9+

## Running Locally

One-command launch:
```bash
docker-compose up postgres redis kafka zookeeper -d && ./mvnw spring-boot:run
```

Manual steps:
1. Start services: `docker-compose up -d postgres redis kafka zookeeper`
2. Build and run: `./mvnw clean package -DskipTests && java -jar target/mini-upi-0.0.1.jar`
3. Open Swagger UI at http://localhost:8080/swagger-ui.html

## Test a Payment

```bash
curl -X POST http://localhost:8080/transactions/send \
  -H "Authorization: Bearer <jwt>" \
  -H "Idempotency-Key: abc123" \
  -H "Content-Type: application/json" \
  -d '{"upi_id":"bob@upi","amount":100}'
```

Always pass a unique `Idempotency-Key` with every send request. Duplicate keys within 24h return the original response without debiting again.

## Environment and Secrets

All sensitive values are passed via environment variables and never written to files tracked by Git.

1. Copy the template: `cp .env.example .env`
2. Fill in your values:

```ini
# .env.example
DB_PASS=replace_with_secure_password
JWT_SECRET=replace_with_random_secret
REDIS_PASS=optional_redis_password
```

3. Docker Compose picks up `.env` automatically.

## Production Docker Compose

```yaml
# docker-compose.prod.yml
services:
  app:
    image: mini-upi:jre21-slim
    ports: ["8080:8080"]
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/upi
      - DB_PASSWORD=${DB_PASS}
      - JWT_SECRET=${JWT_SECRET}
      - REDIS_PASSWORD=${REDIS_PASS}
    restart: always
  postgres:
    image: postgres:15-alpine
    volumes: [pgdata:/var/lib/postgresql/data]
    environment:
      POSTGRES_DB: upi
      POSTGRES_PASSWORD: ${DB_PASS}
volumes:
  pgdata:
```

For secret management in production:
- Docker Swarm: use the `secrets:` block
- Kubernetes: use Secrets objects
- CI/CD: inject at runtime and never log them

## Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre-alpine
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
