package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
}
