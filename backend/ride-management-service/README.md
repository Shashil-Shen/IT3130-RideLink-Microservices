# RideLink Ride Management Service

**Primary Owner: IT24104003**

Port: `8083`

Database: `ridelink_rides`

This service owns ride requests, deterministic driver assignment, lifecycle enforcement, passenger/driver ride history, fare coordination, and driver availability coordination. It never accesses another service's database.

## Environment

- `RIDE_DB_URL`
- `RIDE_DB_USERNAME`
- `RIDE_DB_PASSWORD`
- `JWT_SECRET` (same signing secret used by Account Service in this assignment deployment)
- `ACCOUNT_SERVICE_URL` (normally `http://localhost:8081`)
- `DRIVER_SERVICE_URL` (normally `http://localhost:8082`)
- `FARE_PAYMENT_SERVICE_URL` (normally `http://localhost:8084`)

Never commit real secrets. Connection/read timeouts default to 2/3 seconds.

## Build, test and run

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
```

Swagger UI: `http://localhost:8083/swagger-ui.html`

## Endpoints

See [`docs/api-contracts/ride-management-service-api.md`](../../docs/api-contracts/ride-management-service-api.md). Public operations cover request, lookup, histories, accept, start, complete and cancel. The internal driver-sync endpoint retries a failed terminal-state release.

## Lifecycle rules

`REQUESTED -> ASSIGNED -> ACCEPTED -> IN_PROGRESS -> COMPLETED`. Cancellation is allowed from REQUESTED or ASSIGNED by the owning passenger, and from ACCEPTED by the assigned driver with a reason. COMPLETED and CANCELLED are terminal. See [`docs/ride-lifecycle.md`](../../docs/ride-lifecycle.md).

## Interservice communication

- Account Service validates the authenticated passenger.
- Driver Service finds eligible drivers, reserves the selected driver as `ON_RIDE`, and restores availability after a terminal state.
- Fare Service calculates both estimated and final fare; Ride Management never hardcodes prices.

Eligible drivers are selected by earliest `availableSince`, then driver-profile UUID. A reservation conflict causes the next candidate to be attempted.

## Failure handling

Downstream 4xx/5xx responses, connection failures and timeouts are mapped to stable JSON errors. If local persistence fails after reservation, the driver reservation is compensated. Completion/cancellation is stored before release; a release failure remains `RELEASE_PENDING` and is safely retryable, preventing accidental double assignment.

## Sample workflow

1. PASSENGER requests a CAR ride.
2. Service validates the account, obtains a fare estimate, selects and reserves a driver.
3. Assigned DRIVER accepts and starts the ride.
4. Assigned DRIVER completes it; Fare Service calculates final fare.
5. Driver Service restores availability.

## Limitations

- The current Fare and Payment Service exposes only `/api/fares/estimate`; it is reused for final calculation until that service publishes a dedicated final-fare endpoint.
- Shared HMAC secrets are suitable for this assignment topology but asymmetric keys or a JWKS endpoint are preferable in production.
- Release retries are explicit; a production system should schedule them through an outbox/job worker.
- PostgreSQL Testcontainers coverage requires Docker; H2 PostgreSQL mode is used for the fast persistence suite.
