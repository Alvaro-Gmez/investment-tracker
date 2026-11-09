# Investment Tracker

API para seguimiento de cartera de inversiones personales con fiscalidad española.

## Stack

- Backend: Java 25 · Spring Boot 4 · Spring Data JPA
- Base de datos: PostgreSQL 18 (Docker Compose)
- Frontend: Angular 22 (en desarrollo)

## Requisitos

- JDK 25
- Docker Desktop

## Cómo arrancarlo

1. Base de datos, desde la raíz: `docker compose up -d`
2. API, desde `backend/`: `./mvnw spring-boot:run`
3. La API queda en `http://localhost:8081/api/operations`

## Estado

En desarrollo. Ya funciona: alta de operaciones con validación, posición, importe invertido y número de operaciones por ticker.