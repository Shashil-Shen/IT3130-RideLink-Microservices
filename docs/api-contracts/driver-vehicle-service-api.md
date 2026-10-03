# Driver and Vehicle Service API Contract

Owner: **IT24102084**  
Base URL: `http://localhost:8082/api/v1`

Driver-owned endpoints derive `accountId` from JWT `sub`; clients cannot select another account. Internal endpoints require `ROLE_SERVICE` or `ROLE_ADMIN`.

Eligibility requires `AVAILABLE`, matching normalized `serviceArea`, and an active vehicle of the requested `vehicleType`. Ordering is longest available first and then stable UUID order.

Assignment uses `PUT /internal/drivers/{driverId}/assignments/{rideId}`. Restoration uses `DELETE` on the same URI and requires the matching current ride ID.

All errors contain `timestamp`, `status`, `error`, `message`, `path`, and `validationErrors`. Expected statuses include `201`, `200`, `400`, `401`, `403`, `404`, `409`, `422`, and `503`.
