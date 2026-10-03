-- Run as a PostgreSQL administrator. Only Ride Management Service owns this database.
CREATE DATABASE ridelink_rides;

\connect ridelink_rides

CREATE TABLE rides (
    id UUID PRIMARY KEY,
    passenger_id UUID NOT NULL,
    driver_id UUID,
    driver_profile_id UUID,
    vehicle_id UUID,
    pickup_name VARCHAR(160) NOT NULL,
    destination_name VARCHAR(160) NOT NULL,
    service_area VARCHAR(80) NOT NULL,
    simulated_distance_km NUMERIC(10,2) NOT NULL CHECK (simulated_distance_km > 0),
    vehicle_type VARCHAR(16) NOT NULL CHECK (vehicle_type IN ('BIKE','CAR','VAN')),
    estimated_fare NUMERIC(12,2) NOT NULL CHECK (estimated_fare >= 0),
    final_fare NUMERIC(12,2) CHECK (final_fare >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('REQUESTED','ASSIGNED','ACCEPTED','IN_PROGRESS','COMPLETED','CANCELLED')),
    requested_at TIMESTAMPTZ NOT NULL,
    assigned_at TIMESTAMPTZ,
    accepted_at TIMESTAMPTZ,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    cancelled_at TIMESTAMPTZ,
    cancellation_reason VARCHAR(500),
    driver_sync_status VARCHAR(24) NOT NULL CHECK (driver_sync_status IN ('NOT_REQUIRED','ON_RIDE_CONFIRMED','RELEASE_PENDING','RELEASED')),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CHECK (status NOT IN ('ASSIGNED','ACCEPTED','IN_PROGRESS','COMPLETED') OR (driver_id IS NOT NULL AND driver_profile_id IS NOT NULL AND vehicle_id IS NOT NULL)),
    CHECK (status <> 'COMPLETED' OR (completed_at IS NOT NULL AND final_fare IS NOT NULL)),
    CHECK (status <> 'CANCELLED' OR (cancelled_at IS NOT NULL AND cancellation_reason IS NOT NULL))
);

CREATE INDEX idx_rides_passenger_requested ON rides (passenger_id, requested_at DESC);
CREATE INDEX idx_rides_driver_requested ON rides (driver_id, requested_at DESC);
CREATE INDEX idx_rides_status_requested ON rides (status, requested_at);
CREATE INDEX idx_rides_driver_sync ON rides (driver_sync_status);
