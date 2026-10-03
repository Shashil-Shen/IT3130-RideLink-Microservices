package lk.ac.sliit.ridelink.account.infrastructure.configuration;

import lk.ac.sliit.ridelink.account.application.port.out.PasswordPort;
import lk.ac.sliit.ridelink.account.application.port.out.TokenGenerationPort;
import lk.ac.sliit.ridelink.account.application.usecase.AccountApplicationService;
import lk.ac.sliit.ridelink.account.domain.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    AccountApplicationService accountApplicationService(AccountRepository repository, PasswordPort passwordPort,
                                                        TokenGenerationPort tokenGenerationPort) {
        return new AccountApplicationService(repository, passwordPort, tokenGenerationPort);
    }
}
