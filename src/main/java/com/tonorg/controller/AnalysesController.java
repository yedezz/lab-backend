package com.tonorg.controller;

import com.tonorg.model.LabRequest;
import com.tonorg.model.Patient;
import com.tonorg.repository.LabRequestRepository;
import com.tonorg.repository.PatientRepository;
import com.tonorg.service.FileStorageService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AnalysesController {

    private final LabRequestRepository labRequestRepository;
    private final PatientRepository patientRepository;
    private final FileStorageService fileStorageService;

    // ==================================
    // INJECTION PAR CONSTRUCTEUR
    // ==================================
    public AnalysesController(
            LabRequestRepository labRequestRepository,
            PatientRepository patientRepository,
            FileStorageService fileStorageService
    ) {
        this.labRequestRepository = labRequestRepository;
        this.patientRepository = patientRepository;
        this.fileStorageService = fileStorageService;
    }

    // =========================================================
    // 📜 LISTE DES ANALYSES D’UN PATIENT (LAB / ADMIN)
    // =========================================================
    @GetMapping("/api/patients/{patientId}/analyses")
    public List<LabRequest> getAnalysesByPatient(
            @PathVariable Long patientId
    ) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return labRequestRepository.findByPatient(patient);
    }

    // =========================================================
    // ➕ CRÉER UNE ANALYSE POUR UN PATIENT
    // =========================================================
    @PostMapping("/api/patients/{patientId}/analyses")
    public LabRequest createAnalyse(
            @PathVariable Long patientId,
            @RequestBody LabRequest request
    ) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // sécurité : on force la liaison patient
        request.setId(null);
        request.setPatient(patient);

        return labRequestRepository.save(request);
    }

    // =========================================================
    // 🗑️ SUPPRIMER UNE ANALYSE + PDF MINIO
    // =========================================================
    @DeleteMapping("/api/analyses/{id}")
    public void deleteAnalyse(@PathVariable Long id) {

        LabRequest request = labRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analyse not found"));

        // 🔥 supprimer le PDF dans MinIO
        fileStorageService.deleteFile(request.getPdfObjectName());

        // 🔥 supprimer l’analyse en base
        labRequestRepository.delete(request);
    }
}
