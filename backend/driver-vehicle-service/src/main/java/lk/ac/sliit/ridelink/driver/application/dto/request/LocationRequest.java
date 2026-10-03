package lk.ac.sliit.ridelink.driver.application.dto.request;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record LocationRequest(@NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
 @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude) {}
