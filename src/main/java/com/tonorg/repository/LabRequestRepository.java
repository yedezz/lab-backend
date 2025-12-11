package com.tonorg.repository;

import com.tonorg.model.LabRequest;
import com.tonorg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link LabRequest} entities. Includes a helper method
 * to find all requests belonging to a given patient.
 */
@Repository
public interface LabRequestRepository extends JpaRepository<LabRequest, Long> {
    List<LabRequest> findByPatient(Patient patient);
}