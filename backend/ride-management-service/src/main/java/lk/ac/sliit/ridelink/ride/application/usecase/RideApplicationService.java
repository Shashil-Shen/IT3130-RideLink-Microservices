package lk.ac.sliit.ridelink.ride.application.usecase;

import lk.ac.sliit.ridelink.ride.application.dto.request.CreateRideRequest;
import lk.ac.sliit.ridelink.ride.application.dto.response.*;
import lk.ac.sliit.ridelink.ride.application.mapper.RideApplicationMapper;
import lk.ac.sliit.ridelink.ride.application.port.in.*;
import lk.ac.sliit.ridelink.ride.application.port.out.*;
import lk.ac.sliit.ridelink.ride.domain.enums.*;
import lk.ac.sliit.ridelink.ride.domain.exception.*;
import lk.ac.sliit.ridelink.ride.domain.model.Ride;
import lk.ac.sliit.ridelink.ride.domain.repository.RideRepository;
import java.math.BigDecimal; import java.time.Instant; import java.util.*;

public class RideApplicationService implements RequestRideUseCase,RideLifecycleUseCase,RideQueryUseCase {
    private final RideRepository rides; private final AccountValidationPort accounts; private final DriverAssignmentPort drivers; private final FareCalculationPort fares;
    public RideApplicationService(RideRepository rides,AccountValidationPort accounts,DriverAssignmentPort drivers,FareCalculationPort fares){this.rides=rides;this.accounts=accounts;this.drivers=drivers;this.fares=fares;}

    @Override public RideResponse request(UUID passengerId,CreateRideRequest q){
        AccountValidationData account=accounts.validate(passengerId,"PASSENGER");
        if(account==null||!account.valid()||!"PASSENGER".equals(account.role())||!"ACTIVE".equals(account.status()))throw new PassengerValidationException("A valid ACTIVE PASSENGER account is required");
        BigDecimal estimate=fares.estimate(q.vehicleType(),q.simulatedDistanceKm()); UUID rideId=UUID.randomUUID();
        List<EligibleDriverData> candidates=drivers.eligible(q.serviceArea(),q.vehicleType()).stream().sorted(Comparator.comparing(EligibleDriverData::availableSince,Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(EligibleDriverData::driverId)).toList();
        if(candidates.isEmpty())throw new NoAvailableDriverException();
        for(EligibleDriverData candidate:candidates){
            try{
                drivers.assign(candidate.driverId(),rideId);
                Ride ride=Ride.request(rideId,passengerId,q.pickupName(),q.destinationName(),q.serviceArea(),q.simulatedDistanceKm(),q.vehicleType(),estimate);
                ride.assign(candidate.driverAccountId(),candidate.driverId(),candidate.vehicleId());
                try{return RideApplicationMapper.response(rides.save(ride));}catch(RuntimeException persistenceFailure){try{drivers.restore(candidate.driverId(),rideId);}catch(RuntimeException ignored){persistenceFailure.addSuppressed(ignored);}throw persistenceFailure;}
            }catch(DriverReservationConflictException conflict){/* candidate was taken concurrently; try the next one */}
        }
        throw new NoAvailableDriverException();
    }
    @Override public RideResponse accept(UUID id,UUID driver){validateDriver(driver);Ride r=load(id);r.accept(driver);return save(r);}
    @Override public RideResponse start(UUID id,UUID driver){validateDriver(driver);Ride r=load(id);r.start(driver);return save(r);}
    @Override public RideResponse complete(UUID id,UUID driver){validateDriver(driver);Ride r=load(id);r.complete(driver,fares.finalFare(r.getVehicleType(),r.getSimulatedDistanceKm()));r=rides.save(r);return release(r);}
    @Override public RideResponse cancelByPassenger(UUID id,UUID passenger,String reason){Ride r=load(id);r.cancelByPassenger(passenger,reason);r=rides.save(r);return release(r);}
    @Override public RideResponse cancelByDriver(UUID id,UUID driver,String reason){validateDriver(driver);Ride r=load(id);r.cancelByDriver(driver,reason);r=rides.save(r);return release(r);}
    @Override public RideResponse retryDriverRelease(UUID id){Ride r=load(id);if(r.getDriverSyncStatus()!=DriverSyncStatus.RELEASE_PENDING)throw new InvalidRideTransitionException("Ride has no pending driver release");return release(r);}
    private RideResponse release(Ride r){if(r.getDriverProfileId()!=null){try{drivers.restore(r.getDriverProfileId(),r.getId());r.markReleased();r=rides.save(r);}catch(ExternalServiceException ignored){/* persisted RELEASE_PENDING is deliberately retryable */}}return RideApplicationMapper.response(r);}
    @Override public RideResponse get(UUID id,UUID requester,String role){Ride r=load(id);boolean allowed="ADMIN".equals(role)||("PASSENGER".equals(role)&&r.getPassengerId().equals(requester))||("DRIVER".equals(role)&&requester.equals(r.getDriverId()));if(!allowed)throw new ForbiddenRideOperationException("You do not have access to this ride");return RideApplicationMapper.response(r);}
    @Override public List<RideResponse> passengerRides(UUID id){return rides.findByPassengerId(id).stream().map(RideApplicationMapper::response).toList();}
    @Override public List<RideResponse> driverRides(UUID id){return rides.findByDriverId(id).stream().map(RideApplicationMapper::response).toList();}
    private void validateDriver(UUID id){AccountValidationData account=accounts.validate(id,"DRIVER");if(account==null||!account.valid()||!"DRIVER".equals(account.role())||!"ACTIVE".equals(account.status()))throw new ForbiddenRideOperationException("A valid ACTIVE DRIVER account is required");}
    private Ride load(UUID id){return rides.findById(id).orElseThrow(()->new RideNotFoundException(id));} private RideResponse save(Ride r){return RideApplicationMapper.response(rides.save(r));}
}
