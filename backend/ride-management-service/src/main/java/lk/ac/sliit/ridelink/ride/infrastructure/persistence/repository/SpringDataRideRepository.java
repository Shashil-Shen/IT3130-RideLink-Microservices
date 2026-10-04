package lk.ac.sliit.ridelink.ride.infrastructure.persistence.repository;
import lk.ac.sliit.ridelink.ride.infrastructure.persistence.entity.RideJpaEntity; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface SpringDataRideRepository extends JpaRepository<RideJpaEntity,UUID> { List<RideJpaEntity> findByPassengerIdOrderByRequestedAtDesc(UUID id); List<RideJpaEntity> findByDriverIdOrderByRequestedAtDesc(UUID id); }
