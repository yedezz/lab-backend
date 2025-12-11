package com.tonorg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the laboratory portal backend. This class bootstraps the
 * Spring Boot application. No additional configuration is required here
 * because other components are defined in their own configuration classes.
 */
@SpringBootApplication
public class LabBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabBackendApplication.class, args);
    }
}