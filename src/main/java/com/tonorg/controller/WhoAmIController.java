package com.tonorg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Simple debug endpoint that returns basic information about the
 * currently authenticated user as seen by Spring Security. Very useful
 * when wiring OIDC / Keycloak for the first time.
 */
@RestController
public class WhoAmIController {

    @GetMapping("/whoami")
    public Map<String, Object> whoAmI(@AuthenticationPrincipal OidcUser user,
                                      Authentication authentication) {
        return Map.of(
                "username", user != null ? user.getPreferredUsername() : null,
                "authorities", authentication != null ? authentication.getAuthorities() : null,
                "claims", user != null ? user.getClaims() : Map.of()
        );
    }
}
