# Account Service API Contract

Owner: **IT24102085**

Base URL: `http://localhost:8081/api/v1`

## Public operations

- `POST /auth/register/passengers` creates an ACTIVE passenger.
- `POST /auth/register/drivers` creates an ACTIVE driver.
- `POST /auth/login` returns a Bearer JWT for valid ACTIVE accounts.

Registration accepts `fullName`, `email`, `phoneNumber`, and `password`. The selected endpoint determines the role; clients cannot submit a role.

## Authenticated profile operations

- `GET /accounts/me` returns the account identified by the JWT subject.
- `PATCH /accounts/me` updates only `fullName` and `phoneNumber`.

## Administration

- `PATCH /admin/accounts/{accountId}/status` requires `ADMIN` and accepts `ACTIVE`, `INACTIVE`, or `SUSPENDED`.
- `PATCH /admin/accounts/{accountId}/role` requires `ADMIN` and accepts transitions between `PASSENGER` and `DRIVER`.

Public registration cannot create an ADMIN. Promotion to ADMIN and removal of the ADMIN role are intentionally rejected; administrator accounts require controlled provisioning.

## Interservice contract

- `GET /internal/accounts/{accountId}` returns the account summary.
- `GET /internal/accounts/{accountId}/validation?requiredRole=PASSENGER` returns `accountId`, `role`, `status`, and `valid`.

Internal endpoints require a signed service JWT (`role=SERVICE`, `token_type=SERVICE`) or an administrator JWT. No service may access `ridelink_accounts` directly.

## Error contract

```json
{
  "timestamp": "2026-09-26T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/v1/auth/register/passengers",
  "validationErrors": [
    { "field": "email", "message": "must be a well-formed email address" }
  ]
}
```

Expected responses include `201` registration, `200` success, `400` invalid input, `401` authentication failure, `403` insufficient authority, `404` missing account, and `409` duplicate email.
