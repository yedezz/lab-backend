package com.tonorg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration that applies OAuth2/OpenID Connect for user
 * authentication. All requests are secured by default, with the
 * exception of actuator and error endpoints. The {@link SecurityFilterChain}
 * bean configures Spring Security to use the OAuth2 login mechanism and
 * disables CSRF for simplicity in this example. Method level security is
 * enabled to allow fine‑grained authorization rules in service layers.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for simplicity; for production systems you may
                // want to enable it and configure appropriate tokens.
                .csrf(csrf -> csrf.disable())

                // Allow unauthenticated access to actuator and error pages
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )

                // Enable OAuth2 login with default configuration. The
                // necessary client details must be provided in
                // application.yml under spring.security.oauth2.client.
                .oauth2Login(Customizer.withDefaults())

                // Configure logout to redirect to the home page. Default
                // behaviour will log the user out of the session. You may
                // want to adjust the URL or perform additional cleanup.
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}