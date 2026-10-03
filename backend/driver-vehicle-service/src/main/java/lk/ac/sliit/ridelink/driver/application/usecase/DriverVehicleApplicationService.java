package lk.ac.sliit.ridelink.driver.application.usecase;

import lk.ac.sliit.ridelink.driver.application.dto.request.*; import lk.ac.sliit.ridelink.driver.application.dto.response.*;
import lk.ac.sliit.ridelink.driver.application.mapper.DriverApplicationMapper; import lk.ac.sliit.ridelink.driver.application.port.in.DriverVehicleUseCase;
import lk.ac.sliit.ridelink.driver.application.port.out.AccountValidationPort; import lk.ac.sliit.ridelink.driver.domain.enums.*;
import lk.ac.sliit.ridelink.driver.domain.exception.*; import lk.ac.sliit.ridelink.driver.domain.model.*; import lk.ac.sliit.ridelink.driver.domain.repository.*;
import java.util.*;

public class DriverVehicleApplicationService implements DriverVehicleUseCase {
 private final DriverProfileRepository drivers; private final VehicleRepository vehicles; private final AccountValidationPort accounts;
 public DriverVehicleApplicationService(DriverProfileRepository drivers,VehicleRepository vehicles,AccountValidationPort accounts){this.drivers=drivers;this.vehicles=vehicles;this.accounts=accounts;}
 public DriverProfileResponse createProfile(UUID accountId,CreateDriverProfileRequest r){
  if(!accounts.isActiveDriver(accountId))throw new InvalidDriverAccountException(); if(drivers.existsByAccountId(accountId))throw new IllegalStateException("Driver profile already exists");
  String licence=DriverProfile.normalize(r.licenceNumber()); if(drivers.existsByLicenceNumber(licence))throw new DuplicateLicenceException();
  return response(drivers.save(DriverProfile.create(accountId,licence,r.serviceArea())));
 }
 public DriverProfileResponse getOwnProfile(UUID accountId){return response(byAccount(accountId));}
 public DriverProfileResponse getProfile(UUID driverId){return response(driver(driverId));}
 public DriverProfileResponse updateOwnProfile(UUID accountId,UpdateDriverProfileRequest r){DriverProfile d=byAccount(accountId);String licence=DriverProfile.normalize(r.licenceNumber());if(!licence.equals(d.getLicenceNumber())&&drivers.existsByLicenceNumber(licence))throw new DuplicateLicenceException();d.update(licence,r.serviceArea());return response(drivers.save(d));}
 public DriverProfileResponse updateProfile(UUID driverId,UpdateDriverProfileRequest r){DriverProfile d=driver(driverId);String licence=DriverProfile.normalize(r.licenceNumber());if(!licence.equals(d.getLicenceNumber())&&drivers.existsByLicenceNumber(licence))throw new DuplicateLicenceException();d.update(licence,r.serviceArea());return response(drivers.save(d));}
 public DriverProfileResponse updateAvailability(UUID accountId,AvailabilityStatus status){DriverProfile d=byAccount(accountId);d.changeAvailability(status,vehicles.existsActiveByDriverId(d.getId()));return response(drivers.save(d));}
 public DriverProfileResponse updateLocation(UUID accountId,LocationRequest r){DriverProfile d=byAccount(accountId);d.updateLocation(r.latitude(),r.longitude());return response(drivers.save(d));}
 public DriverProfileResponse updateServiceArea(UUID accountId,String area){DriverProfile d=byAccount(accountId);d.updateServiceArea(area);return response(drivers.save(d));}
 public VehicleResponse registerVehicle(UUID accountId,RegisterVehicleRequest r){DriverProfile d=byAccount(accountId);String reg=Vehicle.normalize(r.registrationNumber());if(vehicles.existsByRegistrationNumber(reg))throw new DuplicateRegistrationException();return DriverApplicationMapper.vehicle(vehicles.save(Vehicle.create(d.getId(),reg,r.make(),r.model(),r.colour(),r.vehicleType(),r.manufacturingYear())));}
 public List<VehicleResponse> getOwnVehicles(UUID accountId){return vehicles.findByDriverId(byAccount(accountId).getId()).stream().map(DriverApplicationMapper::vehicle).toList();}
 public VehicleResponse getVehicle(UUID requester,boolean admin,UUID id){Vehicle v=vehicle(id);authorize(requester,admin,v);return DriverApplicationMapper.vehicle(v);}
 public VehicleResponse updateVehicle(UUID requester,boolean admin,UUID id,UpdateVehicleRequest r){Vehicle v=vehicle(id);authorize(requester,admin,v);v.update(r.make(),r.model(),r.colour(),r.vehicleType(),r.manufacturingYear());return DriverApplicationMapper.vehicle(vehicles.save(v));}
 public VehicleResponse setVehicleActive(UUID requester,boolean admin,UUID id,boolean active){Vehicle v=vehicle(id);authorize(requester,admin,v);v.setActive(active);Vehicle saved=vehicles.save(v);if(!active&&!vehicles.existsActiveByDriverId(v.getDriverId())){DriverProfile d=driver(v.getDriverId());d.forceUnavailableIfNoVehicle();drivers.save(d);}return DriverApplicationMapper.vehicle(saved);}
 public List<EligibleDriverResponse> findEligible(String area,VehicleType type){String key=area.trim().toLowerCase(Locale.ROOT);return drivers.findEligible(key,type).stream().flatMap(d->vehicles.findFirstActiveMatching(d.getId(),type).stream().map(v->new EligibleDriverResponse(d.getId(),d.getAccountId(),v.getId(),type,d.getServiceArea(),d.getCurrentLatitude(),d.getCurrentLongitude(),d.getAvailableSince()))).toList();}
 public DriverProfileResponse assign(UUID driverId,UUID rideId){DriverProfile d=driver(driverId);if(!vehicles.existsActiveByDriverId(d.getId()))throw new InvalidAvailabilityTransitionException("Driver has no active vehicle");d.assign(rideId);return response(drivers.save(d));}
 public DriverProfileResponse restore(UUID driverId,UUID rideId){DriverProfile d=driver(driverId);d.restore(rideId,vehicles.existsActiveByDriverId(driverId));return response(drivers.save(d));}
 private void authorize(UUID requester,boolean admin,Vehicle v){if(!admin&&!driver(v.getDriverId()).getAccountId().equals(requester))throw new ForbiddenOperationException();}
 private DriverProfile byAccount(UUID id){return drivers.findByAccountId(id).orElseThrow(()->DriverProfileNotFoundException.forAccount(id));}
 private DriverProfile driver(UUID id){return drivers.findById(id).orElseThrow(()->new DriverProfileNotFoundException(id));}
 private Vehicle vehicle(UUID id){return vehicles.findById(id).orElseThrow(()->new VehicleNotFoundException(id));}
 private DriverProfileResponse response(DriverProfile d){return DriverApplicationMapper.driver(d,vehicles.findByDriverId(d.getId()));}
}
