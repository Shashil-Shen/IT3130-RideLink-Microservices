# Ride Lifecycle and Coordination

```mermaid
stateDiagram-v2
    [*] --> REQUESTED
    REQUESTED --> ASSIGNED: driver reserved
    REQUESTED --> CANCELLED: passenger
    ASSIGNED --> ACCEPTED: assigned driver
    ASSIGNED --> CANCELLED: passenger
    ACCEPTED --> IN_PROGRESS: assigned driver
    ACCEPTED --> CANCELLED: assigned driver + reason
    IN_PROGRESS --> COMPLETED: assigned driver + final fare
    COMPLETED --> [*]
    CANCELLED --> [*]
```

`COMPLETED` and `CANCELLED` are terminal. A passenger may cancel only `REQUESTED` or `ASSIGNED`. An assigned driver may cancel only `ACCEPTED`, before starting, and must give a reason.

## Request and assignment sequence

```mermaid
sequenceDiagram
    participant P as Passenger
    participant R as Ride Service
    participant A as Account Service
    participant F as Fare Service
    participant D as Driver Service
    P->>R: POST /api/v1/rides
    R->>A: Validate ACTIVE PASSENGER
    R->>F: Calculate estimate
    R->>D: Find eligible drivers
    R->>D: Reserve selected driver ON_RIDE
    R->>R: Persist ASSIGNED ride
    alt local persistence fails
      R->>D: Restore driver (compensation)
    end
    R-->>P: 201 Assigned ride
```

## Completion and release sequence

```mermaid
sequenceDiagram
    participant D as Assigned Driver
    participant R as Ride Service
    participant F as Fare Service
    participant V as Driver Service
    D->>R: Complete ride
    R->>F: Calculate final fare
    R->>R: Persist COMPLETED + RELEASE_PENDING
    R->>V: Restore AVAILABLE
    alt release succeeds
      R->>R: Mark RELEASED
    else service unavailable
      R->>R: Keep RELEASE_PENDING for retry
    end
```
