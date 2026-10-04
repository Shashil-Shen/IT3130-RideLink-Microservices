package lk.ac.sliit.ridelink.account.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lk.ac.sliit.ridelink.account.application.dto.request.LoginRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.RegisterAccountRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.dto.response.AuthenticationResponse;
import lk.ac.sliit.ridelink.account.application.port.in.LoginUseCase;
import lk.ac.sliit.ridelink.account.application.port.in.RegisterAccountUseCase;
import lk.ac.sliit.ridelink.account.application.port.out.PasswordPort;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.model.Account;
import lk.ac.sliit.ridelink.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountApiIntegrationTest 
{
    private static final String TEST_SECRET = "test-only-secret-at-least-thirty-two-characters-long";
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired RegisterAccountUseCase registration;
    @Autowired LoginUseCase login;
    @Autowired AccountRepository accounts;
    @Autowired PasswordPort passwords;

    private String accessToken;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        String email = "passenger-" + System.nanoTime() + "@example.test";
        AccountResponse created = registration.registerPassenger(new RegisterAccountRequest("Fictional Passenger",
                email, "+94771234567", "Strong@123"));
        accountId = created.id();
        AuthenticationResponse authentication = login.login(new LoginRequest(email, "Strong@123"));
        accessToken = authentication.accessToken();
    }

    @Test
    void registersPassengerAndRejectsDuplicateEmail() throws Exception {
        var request = new RegisterAccountRequest("New Passenger", "new-" + System.nanoTime() + "@example.test",
                "+94771234568", "Strong@123");
        String body = objectMapper.writeValueAsString(request);
        mvc.perform(post("/api/v1/auth/register/passengers").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.role").value("PASSENGER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
        mvc.perform(post("/api/v1/auth/register/passengers").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void registersDriverThroughHttp() throws Exception {
        var request = new RegisterAccountRequest("New Driver", "driver-" + System.nanoTime() + "@example.test",
                "+94771234570", "Strong@123");
        mvc.perform(post("/api/v1/auth/register/drivers").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.role").value("DRIVER"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void rejectsInvalidRegistrationInput() throws Exception {
        mvc.perform(post("/api/v1/auth/register/passengers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"\",\"email\":\"bad\",\"phoneNumber\":\"1\",\"password\":\"weak\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    void protectsProfileAndAllowsAuthenticatedProfileUpdate() throws Exception {
        mvc.perform(get("/api/v1/accounts/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/accounts/me").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("PASSENGER"));
        mvc.perform(patch("/api/v1/accounts/me").header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Updated Passenger\",\"phoneNumber\":\"+94771234569\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.fullName").value("Updated Passenger"));
        mvc.perform(patch("/api/v1/accounts/me").header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Still Passenger\",\"phoneNumber\":\"+94771234569\","
                                + "\"role\":\"ADMIN\",\"status\":\"SUSPENDED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("PASSENGER"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void passengerCannotUseAdminOperation() throws Exception {
        mvc.perform(patch("/api/v1/admin/accounts/00000000-0000-0000-0000-000000000001/status")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"INACTIVE\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidPasswordReturnsUnauthorized() throws Exception {
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"missing@example.test\",\"password\":\"Wrong@123\"}"))
                .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void rejectsMalformedExpiredWrongSignatureAndUnknownTypeTokens() throws Exception {
        mvc.perform(get("/api/v1/accounts/me").header("Authorization", "Bearer malformed"))
                .andExpect(status().isUnauthorized()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
        mvc.perform(get("/api/v1/accounts/me").header("Authorization", "Bearer "
                        + token(accountId.toString(), "PASSENGER", "USER", Instant.now().minusSeconds(60), TEST_SECRET)))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/accounts/me").header("Authorization", "Bearer "
                        + token(accountId.toString(), "PASSENGER", "USER", Instant.now().plusSeconds(60),
                        "different-test-secret-at-least-thirty-two-characters")))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/accounts/me").header("Authorization", "Bearer "
                        + token(accountId.toString(), "PASSENGER", "UNKNOWN", Instant.now().plusSeconds(60), TEST_SECRET)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectsInternalEndpointsAndAcceptsValidServiceToken() throws Exception {
        mvc.perform(get("/api/v1/internal/accounts/" + accountId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());

        String serviceToken = token("ride-management-service", "SERVICE", "SERVICE",
                Instant.now().plusSeconds(60), TEST_SECRET);
        mvc.perform(get("/api/v1/internal/accounts/" + accountId + "/validation")
                        .header("Authorization", "Bearer " + serviceToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.valid").value(true));
        mvc.perform(get("/api/v1/internal/accounts/" + accountId)
                        .header("Authorization", "Bearer " + serviceToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
        mvc.perform(get("/api/v1/internal/accounts/not-a-uuid")
                        .header("Authorization", "Bearer " + serviceToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void storesBcryptHashAndAllowsAdminStatusUpdate() throws Exception {
        assertThat(accounts.findById(accountId).orElseThrow().getPasswordHash()).startsWith("$2");

        String adminEmail = "admin-" + System.nanoTime() + "@example.test";
        Account admin = accounts.save(Account.register("Fictional Admin", adminEmail, "+94770000001",
                passwords.hash("Strong@123"), AccountRole.ADMIN));
        String adminToken = login.login(new LoginRequest(admin.getEmail(), "Strong@123")).accessToken();
        mvc.perform(patch("/api/v1/admin/accounts/" + accountId + "/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"SUSPENDED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("SUSPENDED"));
        mvc.perform(patch("/api/v1/admin/accounts/" + accountId + "/role")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"role\":\"DRIVER\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("DRIVER"));
        mvc.perform(patch("/api/v1/admin/accounts/" + accountId + "/role")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"role\":\"ADMIN\"}"))
                .andExpect(status().isBadRequest());
    }

    private String token(String subject, String role, String tokenType, Instant expiry, String secret) {
        Instant now = Instant.now();
        return Jwts.builder().subject(subject).issuer("ridelink-account-service")
                .claim("role", role).claim("token_type", tokenType)
                .issuedAt(Date.from(now.minusSeconds(120))).expiration(Date.from(expiry))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
    }
}
