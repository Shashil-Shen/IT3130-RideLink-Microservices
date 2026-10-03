package lk.ac.sliit.ridelink.driver.infrastructure.configuration;
import lk.ac.sliit.ridelink.driver.application.port.out.AccountValidationPort; import lk.ac.sliit.ridelink.driver.application.usecase.DriverVehicleApplicationService; import lk.ac.sliit.ridelink.driver.domain.repository.*; import org.springframework.context.annotation.*;
@Configuration public class ApplicationConfiguration { @Bean DriverVehicleApplicationService service(DriverProfileRepository d,VehicleRepository v,AccountValidationPort a){return new DriverVehicleApplicationService(d,v,a);} }
