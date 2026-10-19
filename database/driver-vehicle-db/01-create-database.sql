-- Run as a PostgreSQL administrator. Only Driver and Vehicle Service owns this database.
SELECT 'CREATE DATABASE ridelink_drivers'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ridelink_drivers')\gexec

\connect ridelink_drivers

CREATE TABLE IF NOT EXISTS driver_profiles (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL UNIQUE,
    licence_number VARCHAR(50) NOT NULL UNIQUE,
    availability_status VARCHAR(20) NOT NULL
        CHECK (availability_status IN ('OFFLINE', 'AVAILABLE', 'ON_RIDE')),
    service_area VARCHAR(100) NOT NULL,
    service_area_key VARCHAR(100) NOT NULL,
    current_latitude NUMERIC(9,6),
    current_longitude NUMERIC(10,6),
    available_since TIMESTAMPTZ,
    current_ride_id UUID,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL,
    registration_number VARCHAR(30) NOT NULL UNIQUE,
    make VARCHAR(60) NOT NULL,
    model VARCHAR(60) NOT NULL,
    colour VARCHAR(40) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL CHECK (vehicle_type IN ('BIKE', 'CAR', 'VAN')),
    manufacturing_year INTEGER NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_vehicles_driver FOREIGN KEY (driver_id) REFERENCES driver_profiles(id)
);

CREATE INDEX IF NOT EXISTS idx_driver_eligibility
    ON driver_profiles (availability_status, service_area_key);
CREATE INDEX IF NOT EXISTS idx_vehicle_eligibility
    ON vehicles (driver_id, active, vehicle_type);
