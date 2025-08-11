package org.example.mapservice.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/maps/**").hasAnyRole("ADMIN", "OFFICE_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/maps/**").hasAnyRole("ADMIN", "OFFICE_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/maps/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            SecretKey fallbackKey = new SecretKeySpec("fallbackkeyfallbackkey".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            return NimbusJwtDecoder.withSecretKey(fallbackKey).build();
        }
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<String> rolesList = new ArrayList<>();
            Object rolesClaim = jwt.getClaims().get("roles");
            if (rolesClaim instanceof Collection<?>) {
                ((Collection<?>) rolesClaim).forEach(r -> rolesList.add(r.toString()));
            } else {
                Object roleClaim = jwt.getClaims().get("role");
                if (roleClaim != null) rolesList.add(roleClaim.toString());
            }

            // Логируем роли для отладки (можно заменить на proper logger)
            System.out.println("Extracted roles from token: " + rolesList);

            return rolesList.stream()
                    .map(role -> "ROLE_" + role.toUpperCase()) // важен верхний регистр, если в ролях прописано admin, ADMIN и т.п.
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return converter;
    }
}
