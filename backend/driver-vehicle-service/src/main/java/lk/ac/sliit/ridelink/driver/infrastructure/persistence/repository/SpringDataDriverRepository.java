package lk.ac.sliit.ridelink.driver.infrastructure.persistence.repository;
import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType; import lk.ac.sliit.ridelink.driver.infrastructure.persistence.entity.DriverProfileJpaEntity; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.*;
public interface SpringDataDriverRepository extends JpaRepository<DriverProfileJpaEntity,UUID> {
 Optional<DriverProfileJpaEntity> findByAccountId(UUID accountId); boolean existsByAccountId(UUID accountId); boolean existsByLicenceNumberIgnoreCase(String licence);
 @Query("select d from DriverProfileJpaEntity d where d.availabilityStatus=lk.ac.sliit.ridelink.driver.domain.enums.AvailabilityStatus.AVAILABLE and d.serviceAreaKey=:area and exists (select v.id from VehicleJpaEntity v where v.driverId=d.id and v.active=true and v.vehicleType=:type) order by d.availableSince asc,d.id asc")
 List<DriverProfileJpaEntity> findEligible(@Param("area") String area,@Param("type") VehicleType type);
}
