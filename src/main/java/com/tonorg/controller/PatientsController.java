package com.tonorg.controller;
import java.time.LocalDate;
import com.tonorg.dto.PatientCreateRequest;
import com.tonorg.model.Patient;
import com.tonorg.model.User;
import com.tonorg.repository.PatientRepository;
import com.tonorg.repository.UserRepository;
import com.tonorg.service.KeycloakUserService;

import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/api/patients")
@CrossOrigin
public class PatientsController {

    private final PatientRepository repository;
    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    public PatientsController(
            PatientRepository repository,
            UserRepository userRepository,
            KeycloakUserService keycloakUserService
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.keycloakUserService = keycloakUserService;
    }

    // ==========================
    // 📋 LISTE DES PATIENTS
    // ==========================
    @GetMapping
    public List<Patient> getAll() {
        return repository.findAll()
                .stream()
                .filter(p -> !p.isArchived())
                .toList();
    }

    // ==========================
    // ➕ CRÉATION PATIENT
    // ==========================
    @PostMapping
    public Patient create(@RequestBody PatientCreateRequest dto) {

        // 1️⃣ mot de passe = date naissance (ddMMyyyy)
        String tempPassword = dto.dateOfBirth.format(
                DateTimeFormatter.ofPattern("ddMMyyyy")
        );

        // 2️⃣ création Keycloak
        keycloakUserService.createPatient(
                dto.email,       // username = email
                dto.email,
                dto.firstName,
                dto.lastName,
                tempPassword
        );

// 3️⃣ création USER EN BASE
        User user = new User();
        user.setUsername(dto.email);
        user.setRole("PATIENT");
        user.setPasswordHash("KEYCLOAK");
        userRepository.save(user);

// 4️⃣ création PATIENT
        Patient p = new Patient();
        p.setFirstName(dto.firstName);
        p.setLastName(dto.lastName);
        p.setDateOfBirth(dto.dateOfBirth);
        p.setEmail(dto.email);
        p.setPhone(dto.phone);
        p.setCin(dto.cin);
        p.setSocialSecurityNumber(dto.socialSecurityNumber);

// 🔥 LIAISON DES DEUX CÔTÉS
        p.setUser(user);
        repository.save(p);

        user.setPatient(p);
        userRepository.save(user);

        return p;
    }

    private String passwordFromBirthDate(LocalDate dateOfBirth) {
        return dateOfBirth.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    }

    // ==========================
    // ✏️ MODIFIER PATIENT
    // ==========================
    @PutMapping("/{id}")
    public Patient update(
            @PathVariable Long id,
            @RequestBody PatientCreateRequest dto
    ) {
        Patient p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        p.setFirstName(dto.firstName);
        p.setLastName(dto.lastName);
        p.setDateOfBirth(dto.dateOfBirth);
        p.setEmail(dto.email);
        p.setPhone(dto.phone);
        p.setCin(dto.cin);
        p.setSocialSecurityNumber(dto.socialSecurityNumber);

        return repository.save(p);
    }

    // ==========================
    // 🔍 DÉTAIL PATIENT
    // ==========================
    @GetMapping("/{id}")
    public Patient getOne(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    // ==========================
    // 🗑️ ARCHIVER PATIENT
    // ==========================
    @DeleteMapping("/{id}")
    public void archive(@PathVariable Long id) {
        Patient p = repository.findById(id).orElseThrow();
        p.setArchived(true);
        repository.save(p);
    }
}
