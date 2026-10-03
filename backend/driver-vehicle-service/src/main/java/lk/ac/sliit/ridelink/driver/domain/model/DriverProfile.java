package lk.ac.sliit.ridelink.driver.domain.model;

import lk.ac.sliit.ridelink.driver.domain.enums.AvailabilityStatus;
import lk.ac.sliit.ridelink.driver.domain.exception.InvalidAvailabilityTransitionException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public final class DriverProfile {
    private final UUID id;
    private final UUID accountId;
    private String licenceNumber;
    private AvailabilityStatus availabilityStatus;
    private String serviceArea;
    private BigDecimal currentLatitude;
    private BigDecimal currentLongitude;
    private Instant availableSince;
    private UUID currentRideId;
    private final Instant createdAt;
    private Instant updatedAt;

    public DriverProfile(UUID id, UUID accountId, String licenceNumber, AvailabilityStatus availabilityStatus,
                         String serviceArea, BigDecimal latitude, BigDecimal longitude, Instant availableSince,
                         UUID currentRideId, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id); this.accountId = Objects.requireNonNull(accountId);
        this.licenceNumber = normalize(licenceNumber); this.availabilityStatus = Objects.requireNonNull(availabilityStatus);
        this.serviceArea = requireText(serviceArea); this.currentLatitude = latitude; this.currentLongitude = longitude;
        this.availableSince = availableSince; this.currentRideId = currentRideId;
        this.createdAt = Objects.requireNonNull(createdAt); this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static DriverProfile create(UUID accountId, String licenceNumber, String serviceArea) {
        Instant now = Instant.now();
        return new DriverProfile(UUID.randomUUID(), accountId, licenceNumber, AvailabilityStatus.OFFLINE,
                serviceArea, null, null, null, null, now, now);
    }

    public void update(String licenceNumber, String serviceArea) { this.licenceNumber=normalize(licenceNumber); this.serviceArea=requireText(serviceArea); touch(); }
    public void updateServiceArea(String value) { this.serviceArea=requireText(value); touch(); }
    public void updateLocation(BigDecimal latitude, BigDecimal longitude) { validateCoordinates(latitude, longitude); this.currentLatitude=latitude; this.currentLongitude=longitude; touch(); }
    public void changeAvailability(AvailabilityStatus target, boolean hasActiveVehicle) {
        if (availabilityStatus == AvailabilityStatus.ON_RIDE) throw new InvalidAvailabilityTransitionException("An ON_RIDE driver cannot manually change availability");
        if (target == AvailabilityStatus.ON_RIDE) throw new InvalidAvailabilityTransitionException("ON_RIDE is controlled by ride assignment");
        if (target == AvailabilityStatus.AVAILABLE && !hasActiveVehicle) throw new InvalidAvailabilityTransitionException("Driver cannot become AVAILABLE without an active vehicle");
        availabilityStatus=target; availableSince=target==AvailabilityStatus.AVAILABLE?Instant.now():null; touch();
    }
    public void assign(UUID rideId) {
        if (availabilityStatus != AvailabilityStatus.AVAILABLE) throw new InvalidAvailabilityTransitionException("Driver is not available for assignment");
        availabilityStatus=AvailabilityStatus.ON_RIDE; currentRideId=Objects.requireNonNull(rideId); availableSince=null; touch();
    }
    public void restore(UUID rideId, boolean hasActiveVehicle) {
        if (availabilityStatus != AvailabilityStatus.ON_RIDE || !Objects.equals(currentRideId, rideId))
            throw new InvalidAvailabilityTransitionException("Ride does not match the driver's current assignment");
        currentRideId=null; availabilityStatus=hasActiveVehicle?AvailabilityStatus.AVAILABLE:AvailabilityStatus.UNAVAILABLE;
        availableSince=availabilityStatus==AvailabilityStatus.AVAILABLE?Instant.now():null; touch();
    }
    public void forceUnavailableIfNoVehicle() { if (availabilityStatus==AvailabilityStatus.AVAILABLE) { availabilityStatus=AvailabilityStatus.UNAVAILABLE; availableSince=null; touch(); } }
    public String serviceAreaKey() { return serviceArea.trim().toLowerCase(Locale.ROOT); }
    public static String normalize(String value) { return requireText(value).replaceAll("\\s+", "").toUpperCase(Locale.ROOT); }
    private static String requireText(String value) { if(value==null||value.isBlank()) throw new IllegalArgumentException("Value must not be blank"); return value.trim(); }
    private static void validateCoordinates(BigDecimal latitude, BigDecimal longitude) {
        if(latitude==null||longitude==null||latitude.compareTo(BigDecimal.valueOf(-90))<0||latitude.compareTo(BigDecimal.valueOf(90))>0||longitude.compareTo(BigDecimal.valueOf(-180))<0||longitude.compareTo(BigDecimal.valueOf(180))>0)
            throw new IllegalArgumentException("Coordinates are outside valid latitude/longitude ranges");
    }
    private void touch(){updatedAt=Instant.now();}
    public UUID getId(){return id;} public UUID getAccountId(){return accountId;} public String getLicenceNumber(){return licenceNumber;}
    public AvailabilityStatus getAvailabilityStatus(){return availabilityStatus;} public String getServiceArea(){return serviceArea;}
    public BigDecimal getCurrentLatitude(){return currentLatitude;} public BigDecimal getCurrentLongitude(){return currentLongitude;}
    public Instant getAvailableSince(){return availableSince;} public UUID getCurrentRideId(){return currentRideId;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
