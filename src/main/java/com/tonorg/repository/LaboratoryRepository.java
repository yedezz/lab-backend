// src/main/java/com/tonorg/repository/LaboratoryRepository.java
package com.tonorg.repository;

import com.tonorg.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {
}
