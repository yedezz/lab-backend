package com.tonorg.repository;

import com.tonorg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Patient} entities. Provides basic CRUD
 * functionality through Spring Data JPA.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}