package lk.ac.sliit.ridelink.ride.application.port.out;
import lk.ac.sliit.ridelink.ride.application.dto.response.AccountValidationData; import java.util.UUID;
public interface AccountValidationPort { AccountValidationData validate(UUID accountId,String requiredRole); }
