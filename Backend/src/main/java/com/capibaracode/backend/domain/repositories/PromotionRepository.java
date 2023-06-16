package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
}
