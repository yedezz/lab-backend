package com.tonorg.controller;

import com.tonorg.model.LabRequest;
import com.tonorg.model.Patient;
import com.tonorg.model.User;
import com.tonorg.repository.LabRequestRepository;
import com.tonorg.repository.PatientRepository;
import com.tonorg.repository.UserRepository;
import com.tonorg.service.MinioReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller exposing endpoints for authenticated users to list their
 * analysis requests and retrieve pre‑signed URLs for PDF reports. The
 * current user is determined from the authentication principal. In a
 * production environment you may want to further validate the principal
 * and handle different roles (e.g. laboratory staff) separately.
 */
@RestController
public class LabController {

    private final LabRequestRepository labRequestRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final MinioReportService minioReportService;

    public LabController(LabRequestRepository labRequestRepository,
                         UserRepository userRepository,
                         PatientRepository patientRepository,
                         MinioReportService minioReportService) {
        this.labRequestRepository = labRequestRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.minioReportService = minioReportService;
    }

    /**
     * Returns the list of lab requests for the currently authenticated user.
     * The user's username is extracted from the {@link Authentication}
     * principal. If the user or patient is not found, an empty list is
     * returned.
     */
    @GetMapping("/api/me/requests")
    public ResponseEntity<List<LabRequest>> getMyRequests(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        User user = optUser.get();
        Patient patient = user.getPatient();
        if (patient == null) {
            return ResponseEntity.ok(List.of());
        }
        List<LabRequest> requests = labRequestRepository.findByPatient(patient);
        return ResponseEntity.ok(requests);
    }

    /**
     * Generates and returns a pre‑signed URL allowing the currently
     * authenticated user to download the PDF report associated with a given
     * lab request. The method performs a simple ownership check to ensure
     * that the requested lab request belongs to the current user. If not,
     * a 404 response is returned.
     *
     * @param id the identifier of the lab request
     * @return the pre‑signed URL, or 404 if the request cannot be found or
     *         does not belong to the user
     */
    @GetMapping("/api/me/requests/{id}/report")
    public ResponseEntity<String> getMyReport(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optUser.get();
        Patient patient = user.getPatient();
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<LabRequest> optRequest = labRequestRepository.findById(id);
        if (optRequest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        LabRequest request = optRequest.get();
        if (!request.getPatient().equals(patient)) {
            // The request does not belong to the current user
            return ResponseEntity.notFound().build();
        }
        String objectKey = request.getReportObjectKey();
        if (objectKey == null || objectKey.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        try {
            String url = minioReportService.generatePresignedUrl(objectKey, 300);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}