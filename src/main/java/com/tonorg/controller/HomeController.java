package com.tonorg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic landing endpoint so that the application has a root URL
 * returning something meaningful once the user is authenticated.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Lab backend is up. Call /whoami or /api/me/requests after authentication.";
    }
}
