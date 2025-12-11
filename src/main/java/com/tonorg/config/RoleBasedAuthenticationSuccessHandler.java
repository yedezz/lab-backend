package com.tonorg.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String targetUrl = "/"; // fallback

        if (hasRole(authorities, "admin")) {
            targetUrl = "/admin";
        } else if (hasRole(authorities, "labo_admin")) {
            targetUrl = "/lab/admin";
        } else if (hasRole(authorities, "labo_user")) {
            targetUrl = "/lab";
        } else if (hasRole(authorities, "patient")) {
            targetUrl = "/patient";
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        String expected = "ROLE_" + role; // Spring ajoute par défaut le préfixe ROLE_
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(expected::equals);
    }
}
