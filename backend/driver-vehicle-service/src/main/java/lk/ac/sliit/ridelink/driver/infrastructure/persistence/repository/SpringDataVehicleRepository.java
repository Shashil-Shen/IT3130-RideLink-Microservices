package lk.ac.sliit.ridelink.driver.infrastructure.persistence.repository;
import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType; import lk.ac.sliit.ridelink.driver.infrastructure.persistence.entity.VehicleJpaEntity; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface SpringDataVehicleRepository extends JpaRepository<VehicleJpaEntity,UUID> {
 List<VehicleJpaEntity> findByDriverIdOrderById(UUID driverId); boolean existsByRegistrationNumberIgnoreCase(String registration); boolean existsByDriverIdAndActiveTrue(UUID driverId);
 Optional<VehicleJpaEntity> findFirstByDriverIdAndActiveTrueAndVehicleTypeOrderById(UUID driverId,VehicleType type);
}
