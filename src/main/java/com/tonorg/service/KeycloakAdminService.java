package com.tonorg.service;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    private Keycloak getInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")           // on s’authentifie souvent sur master avec admin-cli
                .clientId(clientId)
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    /**
     * Crée un utilisateur dans le realm applicatif avec le rôle realm 'labo_admin'.
     * Retourne l'id Keycloak du user.
     */
    public String createLaboAdminUser(String username, String email, String tempPassword) {
        Keycloak keycloak = getInstance();

        // 1) créer le user
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        // mot de passe temporaire
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(tempPassword);
        cred.setTemporary(true); // demandera changement au premier login

        user.setCredentials(Collections.singletonList(cred));

        var usersResource = keycloak.realm(realm).users();
        var response = usersResource.create(user);
        if (response.getStatus() >= 300) {
            throw new IllegalStateException("Erreur création user Keycloak: " + response.getStatus());
        }

        String userId = response.getLocation().getPath()
                .replaceAll(".*/([^/]+)$", "$1");

        // 2) ajouter rôle realm 'labo_admin'
        var realmRole = keycloak.realm(realm).roles().get("labo_admin").toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(realmRole));

        return userId;
    }
}
