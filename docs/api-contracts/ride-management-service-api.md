# Ride Management Service API Contract

Base URL: `http://localhost:8083`

Authentication: signed Account Service JWT in `Authorization: Bearer <token>`.

| Method | Path | Role | Result |
|---|---|---|---|
| POST | `/api/v1/rides` | PASSENGER | Validates, prices and assigns a ride |
| GET | `/api/v1/rides/{rideId}` | owner or ADMIN | Ride details |
| GET | `/api/v1/rides/passenger/me` | PASSENGER | Passenger history |
| GET | `/api/v1/rides/driver/me` | DRIVER | Assigned-driver history |
| POST | `/api/v1/rides/{rideId}/accept` | assigned DRIVER | ASSIGNED to ACCEPTED |
| POST | `/api/v1/rides/{rideId}/start` | assigned DRIVER | ACCEPTED to IN_PROGRESS |
| POST | `/api/v1/rides/{rideId}/complete` | assigned DRIVER | IN_PROGRESS to COMPLETED |
| POST | `/api/v1/rides/{rideId}/cancel` | owner PASSENGER/assigned DRIVER | Permitted cancellation |
| POST | `/api/v1/internal/rides/{rideId}/driver-sync` | SERVICE/ADMIN | Retry pending driver release |

Creation request:

```json
{"pickupName":"Fort","destinationName":"Airport","serviceArea":"Colombo","simulatedDistanceKm":25.0,"vehicleType":"CAR"}
```

Cancellation request:

```json
{"reason":"Plans changed"}
```

Errors consistently contain `timestamp`, `status`, `error`, `message`, `path`, and `validationErrors`. Expected statuses include 400, 401, 403, 404, 409, 422, 503 and 504.
