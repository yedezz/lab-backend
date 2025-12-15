package com.tonorg.repository;

import com.tonorg.model.LabRequest;
import com.tonorg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRequestRepository extends JpaRepository<LabRequest, Long> {

    // ✅ MÉTHODE MANQUANTE
    List<LabRequest> findByPatient(Patient patient);
}
