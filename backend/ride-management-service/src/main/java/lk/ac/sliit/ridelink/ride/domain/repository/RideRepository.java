package lk.ac.sliit.ridelink.ride.domain.repository;
import lk.ac.sliit.ridelink.ride.domain.model.Ride;
import java.util.*;
public interface RideRepository { Ride save(Ride ride); Optional<Ride> findById(UUID id); List<Ride> findByPassengerId(UUID id); List<Ride> findByDriverId(UUID id); }
