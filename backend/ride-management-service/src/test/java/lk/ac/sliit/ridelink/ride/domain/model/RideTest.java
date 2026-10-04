package lk.ac.sliit.ridelink.ride.domain.model;
import lk.ac.sliit.ridelink.ride.domain.enums.*; import lk.ac.sliit.ridelink.ride.domain.exception.*; import org.junit.jupiter.api.*; import java.math.BigDecimal; import java.util.UUID; import static org.junit.jupiter.api.Assertions.*;
class RideTest {UUID passenger=UUID.randomUUID(),driver=UUID.randomUUID(),profile=UUID.randomUUID(),vehicle=UUID.randomUUID();
 private Ride requested(){return Ride.request(UUID.randomUUID(),passenger,"A","B","Colombo",new BigDecimal("4.5"),VehicleType.CAR,new BigDecimal("850.00"));}
 private Ride assigned(){Ride r=requested();r.assign(driver,profile,vehicle);return r;}
 @Test void requestStartsRequested(){Ride r=requested();assertEquals(RideStatus.REQUESTED,r.getStatus());assertNull(r.getDriverId());}
 @Test void rejectsBlankPickup(){assertThrows(IllegalArgumentException.class,()->Ride.request(UUID.randomUUID(),passenger," ","B","C",BigDecimal.ONE,VehicleType.CAR,BigDecimal.ONE));}
 @Test void rejectsNonPositiveDistance(){assertThrows(IllegalArgumentException.class,()->Ride.request(UUID.randomUUID(),passenger,"A","B","C",BigDecimal.ZERO,VehicleType.CAR,BigDecimal.ONE));}
 @Test void assignmentRecordsDriverAndVehicle(){Ride r=assigned();assertAll(()->assertEquals(RideStatus.ASSIGNED,r.getStatus()),()->assertEquals(driver,r.getDriverId()),()->assertEquals(DriverSyncStatus.ON_RIDE_CONFIRMED,r.getDriverSyncStatus()));}
 @Test void onlyAssignedDriverCanAccept(){Ride r=assigned();assertThrows(ForbiddenRideOperationException.class,()->r.accept(UUID.randomUUID()));}
 @Test void validLifecycleCompletes(){Ride r=assigned();r.accept(driver);r.start(driver);r.complete(driver,new BigDecimal("900"));assertAll(()->assertEquals(RideStatus.COMPLETED,r.getStatus()),()->assertEquals(DriverSyncStatus.RELEASE_PENDING,r.getDriverSyncStatus()),()->assertNotNull(r.getCompletedAt()));}
 @Test void cannotStartBeforeAccept(){Ride r=assigned();assertThrows(InvalidRideTransitionException.class,()->r.start(driver));}
 @Test void passengerMayCancelAssignedRide(){Ride r=assigned();r.cancelByPassenger(passenger,"Changed plans");assertEquals(RideStatus.CANCELLED,r.getStatus());}
 @Test void passengerCannotCancelAnotherRide(){Ride r=assigned();assertThrows(ForbiddenRideOperationException.class,()->r.cancelByPassenger(UUID.randomUUID(),"No"));}
 @Test void driverMayCancelOnlyAcceptedRide(){Ride r=assigned();assertThrows(InvalidRideTransitionException.class,()->r.cancelByDriver(driver,"No"));r.accept(driver);r.cancelByDriver(driver,"Vehicle issue");assertEquals(RideStatus.CANCELLED,r.getStatus());}
 @Test void completedIsTerminal(){Ride r=assigned();r.accept(driver);r.start(driver);r.complete(driver,BigDecimal.TEN);assertThrows(InvalidRideTransitionException.class,()->r.cancelByPassenger(passenger,"No"));}
 @Test void cancelledIsTerminal(){Ride r=assigned();r.cancelByPassenger(passenger,"No longer needed");assertThrows(InvalidRideTransitionException.class,()->r.accept(driver));}
 @Test void releaseRequiresTerminalState(){Ride r=assigned();assertThrows(InvalidRideTransitionException.class,r::markReleased);}
}
