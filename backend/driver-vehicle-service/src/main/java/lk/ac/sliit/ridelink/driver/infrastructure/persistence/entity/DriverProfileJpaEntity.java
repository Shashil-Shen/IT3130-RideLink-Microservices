package lk.ac.sliit.ridelink.driver.infrastructure.persistence.entity;
import jakarta.persistence.*; import lk.ac.sliit.ridelink.driver.domain.enums.AvailabilityStatus; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="driver_profiles",indexes=@Index(name="idx_driver_eligibility",columnList="availability_status,service_area_key"))
public class DriverProfileJpaEntity {
 @Id private UUID id; @Column(name="account_id",nullable=false,unique=true) private UUID accountId;
 @Column(name="licence_number",nullable=false,unique=true,length=50) private String licenceNumber;
 @Enumerated(EnumType.STRING) @Column(name="availability_status",nullable=false,length=20) private AvailabilityStatus availabilityStatus;
 @Column(name="service_area",nullable=false,length=100) private String serviceArea; @Column(name="service_area_key",nullable=false,length=100) private String serviceAreaKey;
 @Column(name="current_latitude",precision=9,scale=6) private BigDecimal currentLatitude; @Column(name="current_longitude",precision=10,scale=6) private BigDecimal currentLongitude;
 @Column(name="available_since") private Instant availableSince; @Column(name="current_ride_id") private UUID currentRideId;
 @Column(name="created_at",nullable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 protected DriverProfileJpaEntity(){}
 public DriverProfileJpaEntity(UUID id,UUID accountId,String licence,AvailabilityStatus status,String area,String areaKey,BigDecimal lat,BigDecimal lon,Instant available,UUID ride,Instant created,Instant updated){this.id=id;this.accountId=accountId;this.licenceNumber=licence;this.availabilityStatus=status;this.serviceArea=area;this.serviceAreaKey=areaKey;this.currentLatitude=lat;this.currentLongitude=lon;this.availableSince=available;this.currentRideId=ride;this.createdAt=created;this.updatedAt=updated;}
 public UUID getId(){return id;} public UUID getAccountId(){return accountId;} public String getLicenceNumber(){return licenceNumber;} public AvailabilityStatus getAvailabilityStatus(){return availabilityStatus;} public String getServiceArea(){return serviceArea;} public BigDecimal getCurrentLatitude(){return currentLatitude;} public BigDecimal getCurrentLongitude(){return currentLongitude;} public Instant getAvailableSince(){return availableSince;} public UUID getCurrentRideId(){return currentRideId;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
