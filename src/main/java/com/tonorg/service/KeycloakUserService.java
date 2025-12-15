package com.tonorg.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakUserService {

    private final Keycloak keycloak;
    private static final String REALM = "medico-cloud";

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    // =====================================================
    // 🔧 MÉTHODE GÉNÉRIQUE INTERNE
    // =====================================================
    private String createUserWithRole(
            String username,
            String email,
            String firstName,
            String lastName,
            String tempPassword,
            String roleName
    ) {

        UsersResource users = keycloak.realm(REALM).users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(lastName);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        Response response = users.create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException(
                    "Erreur création utilisateur Keycloak, status=" + response.getStatus()
            );
        }

        String userId = response.getLocation().getPath()
                .replaceAll(".*/([^/]+)$", "$1");

        // 🔑 mot de passe temporaire
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(tempPassword);
        password.setTemporary(true);

        users.get(userId).resetPassword(password);

        // 🎭 rôle realm
        RoleRepresentation role = keycloak.realm(REALM)
                .roles()
                .get(roleName)
                .toRepresentation();

        users.get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));

        return userId;
    }

    // =====================================================
    // 🧪 LABO ADMIN
    // =====================================================
    public String createLabAdmin(
            String username,
            String email,
            String tempPassword
    ) {
        return createUserWithRole(
                username,
                email,
                null,
                null,
                tempPassword,
                "labo_admin"
        );
    }

    // =====================================================
    // 👤 PATIENT
    // =====================================================
    public String createPatient(
            String username,
            String email,
            String firstName,
            String lastName,
            String tempPassword
    ) {
        return createUserWithRole(
                username,
                email,
                firstName,
                lastName,
                tempPassword,
                "patient"
        );
    }
}
