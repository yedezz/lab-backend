package com.tonorg.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180") // URL Keycloak
                .realm("medico-cloud")                    // realm admin
                .clientId("create_user")   // client confidential
                .clientSecret("ACERaHikIxyZ5BEEB7u0OC6Qa2xs10My")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
