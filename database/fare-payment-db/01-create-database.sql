-- Run as a PostgreSQL administrator. Only Fare and Payment Service owns this database.
SELECT 'CREATE DATABASE ridelink_fare_payments'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ridelink_fare_payments')\gexec

\connect ridelink_fare_payments

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    ride_id UUID NOT NULL UNIQUE,
    passenger_id UUID NOT NULL,
    vehicle_id UUID NOT NULL,
    vehicle_type VARCHAR(32) NOT NULL
        CHECK (vehicle_type IN ('BIKE', 'THREE_WHEELER', 'CAR', 'VAN')),
    distance_km NUMERIC(10,2) NOT NULL CHECK (distance_km > 0),
    base_fare NUMERIC(10,2) NOT NULL CHECK (base_fare >= 0),
    price_per_km NUMERIC(10,2) NOT NULL CHECK (price_per_km >= 0),
    distance_charge NUMERIC(10,2) NOT NULL CHECK (distance_charge >= 0),
    discount NUMERIC(10,2) NOT NULL CHECK (discount >= 0),
    total_fare NUMERIC(10,2) NOT NULL CHECK (total_fare >= 0),
    payment_method VARCHAR(255) NOT NULL,
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED')),
    transaction_reference VARCHAR(255) NOT NULL UNIQUE,
    payment_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_payments_passenger ON payments (passenger_id);
CREATE INDEX IF NOT EXISTS idx_payments_vehicle ON payments (vehicle_id);
