# RideLink Ride Database

**Primary Owner:** IT24104003

**Database:** `ridelink_rides`

Run as a PostgreSQL administrator:

```bash
psql -U postgres -f 01-create-database.sql
```

Configure `RIDE_DB_URL`, `RIDE_DB_USERNAME`, and `RIDE_DB_PASSWORD` through the environment. Hibernate validates the `rides` schema at startup; create the schema using the SQL definition documented in the service README before production use. This database stores only Ride Management data and must never query Account, Driver, Vehicle, Fare, or Payment tables.
