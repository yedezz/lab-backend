package com.tonorg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationSuccessHandler roleBasedSuccessHandler,
                                                   OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .requestMatchers("/lab/admin/**").hasRole("labo_admin")
                        .requestMatchers("/lab/**").hasRole("labo_user")
                        .requestMatchers("/patient/**").hasRole("patient")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService))
                        .successHandler(roleBasedSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return new RoleBasedAuthenticationSuccessHandler();
    }

    /**
     * Custom OidcUserService that maps Keycloak realm roles to Spring authorities.
     */
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            // garder les autorités existantes (ROLE_USER, scopes...)
            mappedAuthorities.addAll(oidcUser.getAuthorities());

            Map<String, Object> claims = oidcUser.getClaims();

            // récupérer realm_access.roles
            Object realmAccessObj = claims.get("realm_access");
            if (realmAccessObj instanceof Map<?, ?> realmAccess) {
                Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof Collection<?> roles) {
                    for (Object role : roles) {
                        String roleName = Objects.toString(role, null);
                        if (roleName != null && !roleName.isBlank()) {
                            mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                        }
                    }
                }
            }

            // on recrée un OidcUser avec les autorités enrichies
            return new DefaultOidcUser(
                    mappedAuthorities,
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );
        };
    }
}
