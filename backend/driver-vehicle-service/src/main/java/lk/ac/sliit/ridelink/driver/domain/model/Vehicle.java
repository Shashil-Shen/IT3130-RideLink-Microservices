package lk.ac.sliit.ridelink.driver.domain.model;

import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType;
import java.time.Instant;
import java.time.Year;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public final class Vehicle {
    private final UUID id; private final UUID driverId; private String registrationNumber; private String make;
    private String model; private String colour; private VehicleType vehicleType; private int manufacturingYear;
    private boolean active; private final Instant createdAt; private Instant updatedAt;
    public Vehicle(UUID id, UUID driverId, String registrationNumber, String make, String model, String colour,
                   VehicleType vehicleType, int manufacturingYear, boolean active, Instant createdAt, Instant updatedAt) {
        this.id=Objects.requireNonNull(id); this.driverId=Objects.requireNonNull(driverId); this.registrationNumber=normalize(registrationNumber);
        this.make=text(make); this.model=text(model); this.colour=text(colour); this.vehicleType=Objects.requireNonNull(vehicleType);
        validateYear(manufacturingYear); this.manufacturingYear=manufacturingYear; this.active=active;
        this.createdAt=Objects.requireNonNull(createdAt); this.updatedAt=Objects.requireNonNull(updatedAt);
    }
    public static Vehicle create(UUID driverId,String registration,String make,String model,String colour,VehicleType type,int year){Instant now=Instant.now();return new Vehicle(UUID.randomUUID(),driverId,registration,make,model,colour,type,year,true,now,now);}
    public void update(String make,String model,String colour,VehicleType type,int year){this.make=text(make);this.model=text(model);this.colour=text(colour);this.vehicleType=Objects.requireNonNull(type);validateYear(year);this.manufacturingYear=year;this.updatedAt=Instant.now();}
    public void setActive(boolean active){this.active=active;this.updatedAt=Instant.now();}
    public static String normalize(String v){return text(v).replaceAll("\\s+","").toUpperCase(Locale.ROOT);}
    private static String text(String v){if(v==null||v.isBlank())throw new IllegalArgumentException("Value must not be blank");return v.trim();}
    private static void validateYear(int year){if(year<1980||year>Year.now().getValue()+1)throw new IllegalArgumentException("Manufacturing year is outside the supported range");}
    public UUID getId(){return id;} public UUID getDriverId(){return driverId;} public String getRegistrationNumber(){return registrationNumber;}
    public String getMake(){return make;} public String getModel(){return model;} public String getColour(){return colour;}
    public VehicleType getVehicleType(){return vehicleType;} public int getManufacturingYear(){return manufacturingYear;}
    public boolean isActive(){return active;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
