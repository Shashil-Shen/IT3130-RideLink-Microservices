package lk.ac.sliit.ridelink.account.infrastructure.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ac.sliit.ridelink.account.domain.repository.AccountRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final AccountRepository accountRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, AccountRepository accountRepository,
                                   AuthenticationEntryPoint authenticationEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.accountRepository = accountRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Claims claims = tokenProvider.parse(header.substring(7));
            String role = claims.get("role", String.class);
            String tokenType = claims.get("token_type", String.class);
            String principal;
            if ("SERVICE".equals(tokenType) && "SERVICE".equals(role)) {
                principal = claims.getSubject();
            } else if ("USER".equals(tokenType)) {
                var account = accountRepository.findById(tokenProvider.accountId(claims))
                        .filter(item -> item.isActive())
                        .orElseThrow(() -> new IllegalStateException("Account is not active"));
                if (!account.getRole().name().equals(role)) {
                    throw new IllegalStateException("Token role does not match account");
                }
                principal = account.getId().toString();
            } else {
                throw new IllegalStateException("Unsupported token type");
            }
            var authentication = new UsernamePasswordAuthenticationToken(principal, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response,
                    new org.springframework.security.authentication.BadCredentialsException("Invalid access token", exception));
        }
    }
}
