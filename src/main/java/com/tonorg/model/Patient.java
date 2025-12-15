package com.tonorg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "cin"),
                @UniqueConstraint(columnNames = "social_security_number")
        }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(unique = true)
    private String cin;

    @Column(name = "social_security_number", unique = true)
    private String socialSecurityNumber;

    @Column(nullable = false)
    private boolean archived = false;
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;
    // 🔗 Lien avec le compte utilisateur
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 🔗 Demandes d’analyses (⚠️ ignorées en JSON)
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LabRequest> requests = new ArrayList<>();
}
