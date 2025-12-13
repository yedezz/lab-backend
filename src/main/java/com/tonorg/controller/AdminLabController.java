package com.tonorg.controller;

import com.tonorg.dto.CreateLabRequest;
import com.tonorg.dto.LabAdminDto;
import com.tonorg.model.Laboratory;
import com.tonorg.model.User;
import com.tonorg.repository.LaboratoryRepository;
import com.tonorg.repository.UserRepository;
import com.tonorg.service.KeycloakUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/labs")
public class AdminLabController {

    private final LaboratoryRepository laboratoryRepository;
    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    public AdminLabController(LaboratoryRepository laboratoryRepository,
                              UserRepository userRepository,
                              KeycloakUserService keycloakUserService) {
        this.laboratoryRepository = laboratoryRepository;
        this.userRepository = userRepository;
        this.keycloakUserService = keycloakUserService;
    }

    /* =========================
       LIST LABS
       ========================= */
    @GetMapping
    public ResponseEntity<?> listLabs() {
        return ResponseEntity.ok(laboratoryRepository.findAll());
    }

    /* =========================
       LIST ADMINS PER LAB
       ========================= */
    @GetMapping("/admins")
    public ResponseEntity<?> listLabAdmins() {

        var result = laboratoryRepository.findAll()
                .stream()
                .filter(lab -> lab.getAdminUser() != null)
                .map(lab -> new LabAdminDto(
                        lab.getId(),
                        lab.getName(),
                        lab.getCity(),
                        lab.getAdminUser().getId(),
                        lab.getAdminUser().getUsername()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    /* =========================
       CREATE LAB + KEYCLOAK USER
       ========================= */
    @PostMapping
    public ResponseEntity<?> createLab(@RequestBody CreateLabRequest request) {

        if (userRepository.existsByUsername(request.getAdminUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Utilisateur déjà existant");
        }

        // 🔐 1️⃣ Créer l'utilisateur dans Keycloak
        keycloakUserService.createLabAdmin(
                request.getAdminUsername(),
                request.getAdminEmail(),
                request.getAdminTempPassword()
        );

        // 💾 2️⃣ Créer l'utilisateur en base
        User adminUser = new User();
        adminUser.setUsername(request.getAdminUsername());
        adminUser.setRole("LAB_ADMIN");
        adminUser.setPasswordHash("KEYCLOAK"); // info uniquement
        userRepository.save(adminUser);

        // 🧪 3️⃣ Créer le laboratoire
        Laboratory lab = new Laboratory();
        lab.setName(request.getLabName());
        lab.setCity(request.getLabCity());
        lab.setContactEmail(request.getLabContactEmail());
        lab.setAdminUser(adminUser);

        laboratoryRepository.save(lab);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
