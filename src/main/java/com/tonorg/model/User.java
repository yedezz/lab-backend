package com.tonorg.model;

import jakarta.persistence.*;

/**
 * Represents a user of the system. In this simplified example, a user
 * corresponds to a single patient account. In a real system you might have
 * additional roles (e.g. lab technicians, administrators) and a mapping
 * table between users and roles. For OIDC authentication the username is
 * typically the email address.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // role can be used to distinguish between patients and lab staff
    private String role;

    // if using local password authentication this field holds the hashed password
    private String passwordHash;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Patient patient;

    public User() {
    }

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}