package lk.ac.sliit.ridelink.driver.infrastructure.persistence.entity;
import jakarta.persistence.*; import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="vehicles",indexes=@Index(name="idx_vehicle_eligibility",columnList="driver_id,active,vehicle_type"))
public class VehicleJpaEntity {
 @Id private UUID id; @Column(name="driver_id",nullable=false) private UUID driverId; @Column(name="registration_number",nullable=false,unique=true,length=30) private String registrationNumber;
 @Column(nullable=false,length=60) private String make; @Column(nullable=false,length=60) private String model; @Column(nullable=false,length=40) private String colour;
 @Enumerated(EnumType.STRING) @Column(name="vehicle_type",nullable=false,length=20) private VehicleType vehicleType; @Column(name="manufacturing_year",nullable=false) private int manufacturingYear;
 @Column(nullable=false) private boolean active; @Column(name="created_at",nullable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt;
 protected VehicleJpaEntity(){}
 public VehicleJpaEntity(UUID id,UUID driverId,String registration,String make,String model,String colour,VehicleType type,int year,boolean active,Instant created,Instant updated){this.id=id;this.driverId=driverId;this.registrationNumber=registration;this.make=make;this.model=model;this.colour=colour;this.vehicleType=type;this.manufacturingYear=year;this.active=active;this.createdAt=created;this.updatedAt=updated;}
 public UUID getId(){return id;} public UUID getDriverId(){return driverId;} public String getRegistrationNumber(){return registrationNumber;} public String getMake(){return make;} public String getModel(){return model;} public String getColour(){return colour;} public VehicleType getVehicleType(){return vehicleType;} public int getManufacturingYear(){return manufacturingYear;} public boolean isActive(){return active;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
