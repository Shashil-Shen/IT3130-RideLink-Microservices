# RideLink Ride Management Service

**Primary Owner: IT23734852**

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