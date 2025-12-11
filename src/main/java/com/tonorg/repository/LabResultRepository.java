package com.tonorg.repository;

import com.tonorg.model.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link LabResult} entities. Provides basic CRUD
 * functionality via Spring Data JPA.
 */
@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
}