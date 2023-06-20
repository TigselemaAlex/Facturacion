package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaxRepository extends JpaRepository<Tax, UUID> {
}
