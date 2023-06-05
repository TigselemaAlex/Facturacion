package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
