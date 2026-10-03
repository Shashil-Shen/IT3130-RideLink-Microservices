# RideLink Driver and Vehicle Service

**Primary Owner:** IT24102084  
**Port:** `8082`  
**Database:** `ridelink_drivers`

Owns driver operational profiles, vehicles, availability, service areas, simulated coordinates, eligibility, assignment state, and availability restoration.

## Environment

`DRIVER_DB_URL`, `DRIVER_DB_USERNAME`, `DRIVER_DB_PASSWORD`, `JWT_SECRET`, and `ACCOUNT_SERVICE_URL` are required. The JWT secret must match Account Service without being committed.

## Commands

```bash
mvn test
mvn clean verify
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Swagger UI: `http://localhost:8082/swagger-ui.html`

## Endpoints

- `/api/v1/drivers/me` - profile creation, retrieval, and update
- `/api/v1/drivers/me/availability` - availability update
- `/api/v1/drivers/me/location` - simulated coordinates
- `/api/v1/drivers/me/service-area` - service area
- `/api/v1/drivers/me/vehicles` - register/list own vehicles
- `/api/v1/vehicles/{id}` - retrieve/update an owned vehicle
- `/api/v1/vehicles/{id}/active` - activate/deactivate
- `/api/v1/admin/drivers/{id}` - administrative retrieval/update
- `/api/v1/internal/drivers/eligible` - deterministic eligibility query
- `/api/v1/internal/drivers/{driverId}/assignments/{rideId}` - assign/restore driver

## Eligibility rule

A driver is eligible only when AVAILABLE, the normalized service area matches, and an active vehicle matches the requested type. Results are ordered by `availableSince`, then driver UUID. Matching vehicles use stable vehicle UUID ordering. Coordinates are simulated and no geographic distance is claimed.

## Account Service interaction

Profile creation calls the Account Service validation endpoint using a short-lived `SERVICE` JWT and requires an ACTIVE DRIVER account. The client uses a two-second connection timeout and three-second response timeout. Downstream unavailability becomes `503 Service Unavailable`.

## Fictional sample data

Licence: `B1234567`; vehicle registration: `WP-CAR-1001`; service area: `Colombo`; coordinates: `6.927079, 79.861244`.

## Limitations

- HMAC sharing allows every trusted service holding the secret to sign tokens; asymmetric signing is preferable in production.
- Locations are simulated and do not use maps or distance calculations.
- The development profile uses Hibernate schema updates; production should use controlled migrations.
