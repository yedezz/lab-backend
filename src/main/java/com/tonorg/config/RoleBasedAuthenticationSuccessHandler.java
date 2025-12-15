package com.tonorg.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class RoleBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // URL de ton front (Vite)
    private static final String FRONT_BASE_URL = "http://localhost:5173";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String targetUrl = determineTargetUrl(authentication);

        // redirige le navigateur vers le front
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin      = hasAuthority(authorities, "ROLE_admin");
        boolean isLaboAdmin  = hasAuthority(authorities, "ROLE_labo_admin");
        boolean isLaboUser   = hasAuthority(authorities, "ROLE_labo_user");
        boolean isPatient    = hasAuthority(authorities, "ROLE_patient");

        if (isAdmin) {
            // route front réservée à l’admin global
            return FRONT_BASE_URL + "/admin";
        } else if (isLaboAdmin) {
            // exemple : route front d’admin de labo
            return FRONT_BASE_URL + "/lab-admin";
        } else if (isLaboUser) {
            return FRONT_BASE_URL + "/lab";
        } else if (isPatient) {
            return FRONT_BASE_URL + "/me";
        }

        // fallback : homepage front
        return FRONT_BASE_URL + "/";
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String expected) {
        return authorities.stream().anyMatch(a -> expected.equals(a.getAuthority()));
    }
}
