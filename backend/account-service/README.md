# RideLink Account Service

**Primary Owner:** IT24102085

**Responsibility:** Identity, registration, authentication, profiles, roles, and account status

**Port:** `8081`

**Database:** `ridelink_accounts`

## Environment variables

| Variable | Purpose |
|---|---|
| `ACCOUNT_DB_URL` | PostgreSQL JDBC URL |
| `ACCOUNT_DB_USERNAME` | Database user |
| `ACCOUNT_DB_PASSWORD` | Database password |
| `JWT_SECRET` | HMAC signing key, minimum 32 characters |
| `JWT_EXPIRATION_MS` | Access-token lifetime in milliseconds |

Never commit real values. Copy the root `.env.example` and supply local values through the runtime environment.

## Commands

```bash
mvn clean verify
mvn test
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Swagger UI: `http://localhost:8081/swagger-ui.html`

OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Endpoint summary

| Method | Path | Access |
|---|---|---|
| POST | `/api/v1/auth/register/passengers` | Public |
| POST | `/api/v1/auth/register/drivers` | Public |
| POST | `/api/v1/auth/login` | Public |
| GET | `/api/v1/accounts/me` | Active authenticated user |
| PATCH | `/api/v1/accounts/me` | Active authenticated user |
| PATCH | `/api/v1/admin/accounts/{id}/status` | ADMIN |
| PATCH | `/api/v1/admin/accounts/{id}/role` | ADMIN |
| GET | `/api/v1/internal/accounts/{id}` | SERVICE or ADMIN |
| GET | `/api/v1/internal/accounts/{id}/validation` | SERVICE or ADMIN |

## Security

Passwords are BCrypt hashes and are never returned by the API. Authentication is stateless. User JWTs contain the account UUID, email, role, issuer, issue time, expiry, and `token_type=USER`. Other RideLink services validate the same HMAC signature using a secret supplied through their environments.

Internal endpoints accept a correctly signed service JWT containing `role=SERVICE` and `token_type=SERVICE`, or an administrator JWT. `SERVICE` is a machine authority and is not an account role. There is intentionally no public endpoint for generating service or administrator tokens.

Passenger and driver roles are assigned by their respective public registration endpoints. An ADMIN may switch an existing non-admin account between PASSENGER and DRIVER. The API intentionally prohibits promotion to ADMIN and removal of the ADMIN role; administrator accounts require controlled provisioning outside the public API.

## Fictional demonstration data

Use only fictional data, for example:

- Passenger: `amal.passenger@example.test`
- Driver: `nimal.driver@example.test`
- Example password format: `Demo@12345`

These are examples only and are not automatically inserted into the database.

## Limitations

- HMAC means every service holding the shared secret can technically sign tokens. Asymmetric signing is recommended for production.
- Existing JWTs are not centrally revoked; short expiry and account validation mitigate this limitation.
- The `dev` profile uses Hibernate schema updates for local convenience. Controlled migrations should be used in production.
- Administrator and service credentials require an explicitly controlled bootstrap process.
