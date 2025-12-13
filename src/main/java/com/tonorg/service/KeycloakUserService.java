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

    private static final String REALM = "medico-cloud"; // ⚠️ ton realm applicatif

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createLabAdmin(String username, String email, String tempPassword) {

        UsersResource users = keycloak.realm(REALM).users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        Response response = null;
        try {
            response = users.create(user);

            int status = response.getStatus();

            String body = "";
            try {
                body = response.readEntity(String.class);
            } catch (Exception ignored) {
            }

            String location = (response.getLocation() != null) ? response.getLocation().toString() : "null";

            System.out.println("KEYCLOAK CREATE USER STATUS = " + status);
            System.out.println("KEYCLOAK CREATE USER LOCATION = " + location);
            System.out.println("KEYCLOAK CREATE USER BODY = " + body);

            if (status != 201) {
                throw new RuntimeException("Erreur création utilisateur Keycloak - status=" + status + " body=" + body);
            }

            // récupérer userId depuis Location
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            // password temporaire
            CredentialRepresentation password = new CredentialRepresentation();
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(tempPassword);
            password.setTemporary(true);

            users.get(userId).resetPassword(password);

            // rôle realm labo_admin
            RoleRepresentation role = keycloak.realm(REALM)
                    .roles()
                    .get("labo_admin")
                    .toRepresentation();

            users.get(userId)
                    .roles()
                    .realmLevel()
                    .add(List.of(role));

        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
