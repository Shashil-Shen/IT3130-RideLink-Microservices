# RideLink Account Database

Primary owner: **IT24102085**

Owning service: **Account Service**

Database: **`ridelink_accounts`**

This folder contains PostgreSQL database initialization resources only. The Account Service is the only service permitted to query or modify this database.

Run the initialization script as a PostgreSQL administrator:

```bash
psql -U postgres -f 01-create-database.sql
```

The script creates the database when needed and then creates the Account Service table, constraints, and indexes. Configure the application using `ACCOUNT_DB_URL`, `ACCOUNT_DB_USERNAME`, and `ACCOUNT_DB_PASSWORD`. Do not commit real credentials. The `dev` profile may update the schema for local convenience; the default profile validates the schema created by this script.
