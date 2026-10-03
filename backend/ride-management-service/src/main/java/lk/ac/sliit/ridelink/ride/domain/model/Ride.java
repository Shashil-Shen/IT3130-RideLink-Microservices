package lk.ac.sliit.ridelink.ride.domain.model;

import lk.ac.sliit.ridelink.ride.domain.enums.*;
import lk.ac.sliit.ridelink.ride.domain.exception.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class Ride {
    private final UUID id; private final UUID passengerId; private UUID driverId; private UUID driverProfileId; private UUID vehicleId;
    private final String pickupName; private final String destinationName; private final String serviceArea; private final BigDecimal simulatedDistanceKm;
    private final VehicleType vehicleType; private BigDecimal estimatedFare; private BigDecimal finalFare; private RideStatus status;
    private final Instant requestedAt; private Instant assignedAt; private Instant acceptedAt; private Instant startedAt; private Instant completedAt;
    private Instant cancelledAt; private String cancellationReason; private DriverSyncStatus driverSyncStatus; private final Instant createdAt; private Instant updatedAt; private long version;

    public Ride(UUID id, UUID passengerId, UUID driverId, UUID driverProfileId, UUID vehicleId, String pickupName, String destinationName,
                String serviceArea, BigDecimal simulatedDistanceKm, VehicleType vehicleType, BigDecimal estimatedFare, BigDecimal finalFare,
                RideStatus status, Instant requestedAt, Instant assignedAt, Instant acceptedAt, Instant startedAt, Instant completedAt,
                Instant cancelledAt, String cancellationReason, DriverSyncStatus driverSyncStatus, Instant createdAt, Instant updatedAt, long version) {
        this.id=Objects.requireNonNull(id); this.passengerId=Objects.requireNonNull(passengerId); this.driverId=driverId; this.driverProfileId=driverProfileId; this.vehicleId=vehicleId;
        this.pickupName=text(pickupName,"Pickup name"); this.destinationName=text(destinationName,"Destination name"); this.serviceArea=text(serviceArea,"Service area");
        if(simulatedDistanceKm==null||simulatedDistanceKm.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("Simulated distance must be greater than zero");
        this.simulatedDistanceKm=simulatedDistanceKm; this.vehicleType=Objects.requireNonNull(vehicleType); this.estimatedFare=estimatedFare; this.finalFare=finalFare;
        this.status=Objects.requireNonNull(status); this.requestedAt=Objects.requireNonNull(requestedAt); this.assignedAt=assignedAt; this.acceptedAt=acceptedAt;
        this.startedAt=startedAt; this.completedAt=completedAt; this.cancelledAt=cancelledAt; this.cancellationReason=cancellationReason;
        this.driverSyncStatus=Objects.requireNonNull(driverSyncStatus); this.createdAt=Objects.requireNonNull(createdAt); this.updatedAt=Objects.requireNonNull(updatedAt); this.version=version;
    }
    public static Ride request(UUID id,UUID passengerId,String pickup,String destination,String area,BigDecimal distance,VehicleType type,BigDecimal estimate){Instant now=Instant.now();return new Ride(id,passengerId,null,null,null,pickup,destination,area,distance,type,nonNegative(estimate,"Estimated fare"),null,RideStatus.REQUESTED,now,null,null,null,null,null,null,DriverSyncStatus.NOT_REQUIRED,now,now,0);}
    public void assign(UUID driverAccountId,UUID profileId,UUID selectedVehicleId){require(RideStatus.REQUESTED,RideStatus.ASSIGNED);driverId=Objects.requireNonNull(driverAccountId);driverProfileId=Objects.requireNonNull(profileId);vehicleId=Objects.requireNonNull(selectedVehicleId);status=RideStatus.ASSIGNED;assignedAt=Instant.now();driverSyncStatus=DriverSyncStatus.ON_RIDE_CONFIRMED;touch();}
    public void accept(UUID actor){assigned(actor);require(RideStatus.ASSIGNED,RideStatus.ACCEPTED);status=RideStatus.ACCEPTED;acceptedAt=Instant.now();touch();}
    public void start(UUID actor){assigned(actor);require(RideStatus.ACCEPTED,RideStatus.IN_PROGRESS);status=RideStatus.IN_PROGRESS;startedAt=Instant.now();touch();}
    public void complete(UUID actor,BigDecimal fare){assigned(actor);require(RideStatus.IN_PROGRESS,RideStatus.COMPLETED);finalFare=nonNegative(fare,"Final fare");status=RideStatus.COMPLETED;completedAt=Instant.now();driverSyncStatus=DriverSyncStatus.RELEASE_PENDING;touch();}
    public void cancelByPassenger(UUID actor,String reason){if(!passengerId.equals(actor))throw new ForbiddenRideOperationException("Passenger cannot control another passenger's ride");if(status!=RideStatus.REQUESTED&&status!=RideStatus.ASSIGNED)throw invalid(RideStatus.CANCELLED);cancel(reason);}
    public void cancelByDriver(UUID actor,String reason){assigned(actor);if(status!=RideStatus.ACCEPTED)throw new InvalidRideTransitionException("Assigned driver may cancel only an ACCEPTED ride before it starts");cancel(reason);}
    private void cancel(String reason){cancellationReason=text(reason,"Cancellation reason");status=RideStatus.CANCELLED;cancelledAt=Instant.now();driverSyncStatus=driverProfileId==null?DriverSyncStatus.NOT_REQUIRED:DriverSyncStatus.RELEASE_PENDING;touch();}
    public void markReleased(){if(driverProfileId==null){driverSyncStatus=DriverSyncStatus.NOT_REQUIRED;return;}if(status!=RideStatus.COMPLETED&&status!=RideStatus.CANCELLED)throw new InvalidRideTransitionException("Driver can be released only for a terminal ride");driverSyncStatus=DriverSyncStatus.RELEASED;touch();}
    private void assigned(UUID actor){if(driverId==null||!driverId.equals(actor))throw new ForbiddenRideOperationException("Only the assigned driver may perform this operation");}
    private void require(RideStatus from,RideStatus to){if(status!=from)throw invalid(to);}
    private InvalidRideTransitionException invalid(RideStatus to){return new InvalidRideTransitionException("Transition from "+status+" to "+to+" is not allowed");}
    private static String text(String v,String name){if(v==null||v.isBlank())throw new IllegalArgumentException(name+" must not be empty");return v.trim();}
    private static BigDecimal nonNegative(BigDecimal v,String name){if(v==null||v.compareTo(BigDecimal.ZERO)<0)throw new IllegalArgumentException(name+" must not be negative");return v;}
    private void touch(){updatedAt=Instant.now();}
    public UUID getId(){return id;} public UUID getPassengerId(){return passengerId;} public UUID getDriverId(){return driverId;} public UUID getDriverProfileId(){return driverProfileId;} public UUID getVehicleId(){return vehicleId;} public String getPickupName(){return pickupName;} public String getDestinationName(){return destinationName;} public String getServiceArea(){return serviceArea;} public BigDecimal getSimulatedDistanceKm(){return simulatedDistanceKm;} public VehicleType getVehicleType(){return vehicleType;} public BigDecimal getEstimatedFare(){return estimatedFare;} public BigDecimal getFinalFare(){return finalFare;} public RideStatus getStatus(){return status;} public Instant getRequestedAt(){return requestedAt;} public Instant getAssignedAt(){return assignedAt;} public Instant getAcceptedAt(){return acceptedAt;} public Instant getStartedAt(){return startedAt;} public Instant getCompletedAt(){return completedAt;} public Instant getCancelledAt(){return cancelledAt;} public String getCancellationReason(){return cancellationReason;} public DriverSyncStatus getDriverSyncStatus(){return driverSyncStatus;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public long getVersion(){return version;}
}
