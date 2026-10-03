-- Run with psql as a PostgreSQL administrator. Application tables are owned by Account Service.
-- The conditional database creation makes this script safe to run more than once.
SELECT 'CREATE DATABASE ridelink_accounts'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ridelink_accounts')\gexec

\connect ridelink_accounts

CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('PASSENGER', 'DRIVER', 'ADMIN')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_accounts_email_lower ON accounts (LOWER(email));
CREATE INDEX IF NOT EXISTS idx_accounts_role_status ON accounts (role, status);
