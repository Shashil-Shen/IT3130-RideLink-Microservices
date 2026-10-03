package lk.ac.sliit.ridelink.ride.infrastructure.configuration;
import lk.ac.sliit.ridelink.ride.application.port.out.*; import lk.ac.sliit.ridelink.ride.application.usecase.RideApplicationService; import lk.ac.sliit.ridelink.ride.domain.repository.RideRepository; import org.springframework.context.annotation.*;
@Configuration public class ApplicationConfiguration { @Bean RideApplicationService rideApplicationService(RideRepository rides,AccountValidationPort accounts,DriverAssignmentPort drivers,FareCalculationPort fares){return new RideApplicationService(rides,accounts,drivers,fares);} }
