# Online Banking Simulation System (Spring Boot)

A Spring Boot based enterprise backend that simulates core online banking operations:
customer management, account management, deposits, withdrawals, fund transfers, and
transaction history — exposed through a REST API and backed by a relational database.

## Tech Stack
- Java 17, Spring Boot 3.3.4
- Spring Web, Spring Data JPA, Spring Validation
- H2 (default, in-memory, zero setup) / MySQL (production profile)
- Maven

## Running the project

```bash
mvn clean install
mvn spring-boot:run
```

The app starts on **http://localhost:8080** using an in-memory H2 database by default
(no setup required). The H2 console is available at `/h2-console`
(JDBC URL: `jdbc:h2:mem:bankingsim`, user: `sa`, no password).

To use MySQL instead, either set environment variables (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_DRIVER`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`) or edit `src/main/resources/application.properties` as described in the comments at the bottom of that file.

## Deployment

### 1. Executable JAR
```bash
mvn clean package
java -jar target/online-banking-simulation.jar
```

### 2. Docker / Cloud Platforms (Render, Railway, Fly.io)
A multi-stage `Dockerfile` is included:
```bash
docker build -t online-banking-simulation .
docker run -p 8080:8080 online-banking-simulation
```
Supported environment variables:
- `PORT` (defaults to `8080`)
- `SPRING_DATASOURCE_URL` (defaults to in-memory H2 `jdbc:h2:mem:bankingsim;DB_CLOSE_DELAY=-1`)
- `SPRING_DATASOURCE_DRIVER` (defaults to `org.h2.Driver`)
- `SPRING_DATASOURCE_USERNAME` (defaults to `sa`)
- `SPRING_DATASOURCE_PASSWORD` (defaults to empty)
- `H2_CONSOLE_ENABLED` (defaults to `true`)

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/customers | Create customer |
| GET | /api/customers/{id} | Get customer |
| POST | /api/accounts | Open account |
| GET | /api/accounts/{id} | Get account |
| POST | /api/accounts/{id}/deposit | Deposit funds |
| POST | /api/accounts/{id}/withdraw | Withdraw funds |
| POST | /api/transactions/transfer | Transfer funds |
| GET | /api/accounts/{id}/transactions | Transaction history |

See `postman_collection.json` for ready-to-import sample requests.
