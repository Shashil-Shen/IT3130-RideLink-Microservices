# RideLink Driver and Vehicle Database

**Primary Owner:** IT24102084  
**Database:** `ridelink_drivers`

Run as a PostgreSQL administrator:

```bash
psql -U postgres -f 01-create-database.sql
```

Configure `DRIVER_DB_URL`, `DRIVER_DB_USERNAME`, and `DRIVER_DB_PASSWORD` through the environment. This database contains only driver operational profiles and vehicles. It must never contain or directly query Account, Ride, Fare, or Payment tables.
