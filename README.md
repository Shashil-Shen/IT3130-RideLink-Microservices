# RideLink Microservices

Backend microservices for the RideLink ride-sharing platform - IT3130 Group Assignment.

## Group Members and Service Ownership

| Student ID | Primary Responsibility |

| IT24102085 | Account Service |
| IT24102084 | Driver and Vehicle Service |
| IT23734852 | Ride Management Service |
| IT24104003 | Fare and Payment Service |

Although each member is the primary owner of one microservice,
all members participate in architecture, API-contract, integration,
testing and code-review decisions.

## Local backend setup

1. Use Java 21 and PostgreSQL.
2. Copy `.env.example` values into your terminal or IDE run configuration and replace placeholder passwords and the JWT secret.
3. Run each `database/*/01-create-database.sql` file with `psql` as a PostgreSQL administrator.
4. Start each service from its own directory with `mvnw.cmd spring-boot:run` on Windows or `./mvnw spring-boot:run` on macOS/Linux.

The services listen on `8081` (account), `8082` (driver/vehicle), `8083` (ride management), and `8084` (fare/payment). Use the same `JWT_SECRET` for the three secured services.

Run `mvnw.cmd test` in every service directory before committing. Each service includes its own Maven Wrapper, so a global Maven installation is not required.
