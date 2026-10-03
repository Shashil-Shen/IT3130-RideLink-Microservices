package lk.ac.sliit.ridelink.driver.infrastructure.configuration;
import org.springframework.context.annotation.*; import org.springframework.http.client.SimpleClientHttpRequestFactory; import org.springframework.web.client.RestClient; import java.time.Duration;
@Configuration public class RestClientConfiguration { @Bean RestClient accountRestClient(DriverProperties p){var f=new SimpleClientHttpRequestFactory();f.setConnectTimeout(Duration.ofSeconds(2));f.setReadTimeout(Duration.ofSeconds(3));return RestClient.builder().baseUrl(p.accountServiceUrl()).requestFactory(f).build();} }
