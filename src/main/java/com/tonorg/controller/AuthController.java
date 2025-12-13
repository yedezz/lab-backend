package com.tonorg.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class AuthController {

    // adapte ces 3 valeurs
    private static final String KEYCLOAK_BASE = "http://localhost:8180";
    private static final String REALM = "medico-cloud";
    private static final String FRONT_URL = "http://localhost:5173";

    @GetMapping("/logout-success")
    public String logoutSuccess(HttpServletRequest request, Authentication authentication) {

        // Par défaut : revenir au front
        String postLogoutRedirect = FRONT_URL;

        // Si on a un id_token, Keycloak conseille de le passer en hint
        String idToken = null;
        if (authentication instanceof OAuth2AuthenticationToken token) {
            Object principal = token.getPrincipal();
            if (principal instanceof OidcUser oidcUser) {
                idToken = oidcUser.getIdToken().getTokenValue();
            }
        }

        String redirect = KEYCLOAK_BASE + "/realms/" + REALM + "/protocol/openid-connect/logout"
                + "?post_logout_redirect_uri=" + URLEncoder.encode(postLogoutRedirect, StandardCharsets.UTF_8);

        if (idToken != null) {
            redirect += "&id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8);
        }

        return "redirect:" + redirect;
    }
}
